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
package io.gravitee.gateway.core.policy;

import io.gravitee.gateway.api.PolicyChain;

/**
 * @author David BRASSELY (brasseld at gmail.com)
 */
public interface PolicyChainBuilder<T extends PolicyChain, R> {

    /**
     * Create a new Policy chain based on the given input object.
     * Input object can be a {@link io.gravitee.gateway.api.Request} or a
     * {@link io.gravitee.gateway.api.Response}.
     *
     * @return A Policy chain handler.
     */
    T newPolicyChain(R input);
}
