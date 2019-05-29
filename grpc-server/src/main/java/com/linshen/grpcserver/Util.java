package com.linshen.grpcserver;

import org.datavec.image.loader.NativeImageLoader;
import org.deeplearning4j.nn.modelimport.keras.KerasModelImport;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
public class Util {
    NativeImageLoader LOADER;
    MultiLayerNetwork model;
    public Util(){
        try {
            this.LOADER=new NativeImageLoader(96, 96, 3);
            this.model=KerasModelImport.importKerasSequentialModelAndWeights(new ClassPathResource("model-38-0.99.h5").getFile().getPath());
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
