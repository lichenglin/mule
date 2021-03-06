/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.module.http.internal.listener.grizzly;

import org.mule.module.http.api.HttpHeaders;
import org.mule.module.http.internal.domain.response.HttpResponse;

import java.util.Collection;

import org.glassfish.grizzly.EmptyCompletionHandler;
import org.glassfish.grizzly.WriteResult;
import org.glassfish.grizzly.http.HttpRequestPacket;
import org.glassfish.grizzly.http.HttpResponsePacket;

public abstract class BaseResponseCompletionHandler extends EmptyCompletionHandler<WriteResult>
{

    protected HttpResponsePacket buildHttpResponsePacket(HttpRequestPacket sourceRequest, HttpResponse httpResponse)
    {
        final HttpResponsePacket.Builder responsePacketBuilder = HttpResponsePacket.builder(sourceRequest)
                .status(httpResponse.getStatusCode())
                .reasonPhrase(httpResponse.getReasonPhrase());

        final Collection<String> allHeaders = httpResponse.getHeaderNames();
        for (String headerName : allHeaders)
        {
            final Collection<String> values = httpResponse.getHeaderValues(headerName);
            for (String value : values)
            {
                responsePacketBuilder.header(headerName, value);
            }
        }
        HttpResponsePacket httpResponsePacket = responsePacketBuilder.build();
        if (httpResponse.getHeaderValue(HttpHeaders.Names.TRANSFER_ENCODING) != null)
        {
            httpResponsePacket.setChunked(true);
        }
        return httpResponsePacket;
    }

}
