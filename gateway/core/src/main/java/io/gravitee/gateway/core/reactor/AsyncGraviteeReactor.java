/**
 * Copyright (C) 2015 The Gravitee team (http://gravitee.io)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.gravitee.gateway.core.reactor;

import io.gravitee.common.http.HttpStatusCode;
import io.gravitee.gateway.api.Request;
import io.gravitee.gateway.api.Response;
import io.gravitee.gateway.core.http.ServerResponse;
import io.gravitee.gateway.core.http.client.HttpClient;
import io.gravitee.gateway.core.http.client.HttpClientFactory;
import io.gravitee.gateway.core.policy.PolicyChainBuilder;
import io.gravitee.gateway.core.policy.RequestPolicyChain;
import io.gravitee.gateway.core.policy.ResponsePolicyChain;
import io.gravitee.model.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import rx.Observable;
import rx.Subscriber;

/**
 * @author David BRASSELY (brasseld at gmail.com)
 */
public class AsyncGraviteeReactor extends GraviteeReactor<Observable<Response>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncGraviteeReactor.class);

    @Autowired
    private RouteMatcher routeMatcher;

    @Autowired
    private PolicyChainBuilder<RequestPolicyChain, Request> requestPolicyChainBuilder;

    @Autowired
    private PolicyChainBuilder<ResponsePolicyChain, Request> responsePolicyChainBuilder;

    @Autowired
    private HttpClientFactory httpClientFactory;

    @Override
    public Observable<Response> process(final Request request) {
        LOGGER.debug("Receiving a request {} for path {}", request.id(), request.path());

        final Api api = routeMatcher.match(request);
        final ServerResponse response = new ServerResponse();

        if (api == null) {
            LOGGER.warn("No API can be found to match request path {}, returning 404", request.path());

            response.setStatus(HttpStatusCode.NOT_FOUND_404);
            return Observable.just((Response)response);
        }

        return handle(api, request, response);
    }

    private Observable<Response> handle(final Api api, final Request request, final Response response) {

        return Observable.create(
                new Observable.OnSubscribe<Response>() {

                    @Override
                    public void call(final Subscriber<? super Response> observer) {
                        // 1_ Apply request policies
                        requestPolicyChainBuilder.newPolicyChain(request).doNext(request, response);

                        // TODO: How to now that something goes wrong in policy chain and skip
                        // remote service invocation...

                        // TODO: remove this part from each call...

                        // 2_ Call remote service
                        HttpClient client = httpClientFactory.create(api);

                        client.invoke(request, response).subscribe(new Subscriber<Response>() {
                            @Override
                            public void onCompleted() {
                                observer.onCompleted();
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                observer.onError(throwable);
                            }

                            @Override
                            public void onNext(Response response) {
                                responsePolicyChainBuilder.newPolicyChain(request).doNext(request, response);
                                observer.onNext(response);
                            }
                        });
                    }
                }
        );
    }
}
