package com.netty.authority.nio.netty.multiprotocol.netty.codec;

import org.jboss.marshalling.Marshaller;
import org.jboss.marshalling.MarshallerFactory;
import org.jboss.marshalling.Marshalling;
import org.jboss.marshalling.MarshallingConfiguration;
import org.jboss.marshalling.Unmarshaller;

import java.io.IOException;

// --------------------- Change Logs----------------------
// <p>@author ruirui.qu Initial Created at 2019/2/20<p>
// -------------------------------------------------------

public class MarshallingCodecFactory {

    protected static Marshaller buildMarshalling() throws IOException {

        final MarshallerFactory marshallerFactory = Marshalling.getProvidedMarshallerFactory("serial");

        final MarshallingConfiguration configuration = new MarshallingConfiguration();

        configuration.setVersion(5);

        Marshaller marshaller = marshallerFactory.createMarshaller(configuration);

        return marshaller;
    }

    protected static Unmarshaller buildUnMarshalling() throws IOException {

        final MarshallerFactory marshallerFactory = Marshalling.getProvidedMarshallerFactory("serial");

        final MarshallingConfiguration configuration = new MarshallingConfiguration();

        configuration.setVersion(5);

        final Unmarshaller unmarshaller = marshallerFactory.createUnmarshaller(configuration);

        return unmarshaller;
    }
}