package com.cezarykluczynski.stapi.server.configuration.interceptor;

import org.apache.cxf.interceptor.Interceptor;
import org.apache.cxf.message.Message;

public interface ApiLimitingInterceptor extends Interceptor<Message> {
}
