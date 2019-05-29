package com.linshen.grpcserver;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.ImagePreProcessingScaler;
import org.nd4j.linalg.factory.Nd4j;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;

import static org.opencv.core.CvType.CV_32FC3;

@Component
public class Core {
        @Autowired
        Util util;
        public int[] doservice(String filepath) {
            try {
                File file = new File(filepath);
                File[] files;
                if (file.isDirectory()){
                     files = file.listFiles();
                }
                else{
                     files=new File[1];
                     files[0]=file;
                }
                ArrayList<INDArray> vectors = new ArrayList<INDArray>();
                //You need this loop if you submitted more than one file
                int i = 0;
                DataNormalization scaler = new ImagePreProcessingScaler(0, 1);
                for (File f : files) {
                    //String fileName = extractFileName(part);
                    //part.write(savePath + File.separator + fileName);
                    Mat image = Imgcodecs.imread(f.getAbsolutePath());
                    Imgproc.cvtColor(image, image, Imgproc.COLOR_BGR2RGB);
                    Imgproc.resize(image, image,
                            new Size(96, 96));
                    image.convertTo(image, CV_32FC3);

                    INDArray indArray = util.LOADER.asMatrix(image);
                    System.out.println(indArray);
                    scaler.transform(indArray);

                    vectors.add(indArray);
                }

                INDArray all_indArray = Nd4j.vstack(vectors);
                //Imgcodecs.imwrite("C:\\input.jpg", image);

                //INDArray transpose = transpose(indArray);
                int[] predict = util.model.predict(all_indArray);
                for (int p : predict) {
                    System.out.println(p);
                }
                return predict;
            } catch (Exception e) {
                e.printStackTrace();
                return new int[1];
            }
        }
}
