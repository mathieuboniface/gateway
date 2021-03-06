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
package io.gravitee.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.gravitee.model.jackson.URIDeserializer;

import java.net.URI;
import java.util.Objects;

/**
 * Defines end points used by the gateway proxy.
 *
 * @author David BRASSELY (brasseld at gmail.com)
 */
public class Api {

    private String name;

    @JsonIgnore
    private String version = "unknown";

    @JsonProperty("public")
    private URI publicURI;

    @JsonProperty("target")
    private URI targetURI;

    @JsonIgnore
    private boolean enabled = true;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public URI getTargetURI() {
        return this.targetURI;
    }

    @JsonDeserialize(using = URIDeserializer.class)
    public void setTargetURI(URI targetURI) {
        this.targetURI = targetURI;
    }

    public URI getPublicURI() {
        return publicURI;
    }

    @JsonDeserialize(using = URIDeserializer.class)
    public void setPublicURI(URI publicURI) {
        this.publicURI = publicURI;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Api api = (Api) o;
        return Objects.equals(name, api.name) &&
                Objects.equals(version, api.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, version);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Api{");
        sb.append("name='").append(name).append('\'');
        sb.append(", version='").append(version).append('\'');
        sb.append(", publicURI=").append(publicURI);
        sb.append(", targetURI=").append(targetURI);
        sb.append(", enabled=").append(enabled);
        sb.append('}');
        return sb.toString();
    }
}
