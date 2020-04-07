package com.gdut.generator.controller;

import com.gdut.generator.model.Exercises;
import com.gdut.generator.service.impl.GenerateServiceImpl;

/**
 * @author <a href="mailto:kobe524348@gmail.com">黄钰朝</a>
 * @description
 * @date 2020-03-27 15:42
 */
public class Controller {

    public static void main(String args[]){
        /*
        GenerateServiceImpl generateService = new GenerateServiceImpl();
        for(int i=0;i<10;i++){
            String e = generateService.generateNum(10);
            System.out.println(e);
        }
    }
    */
        Exercises e = new Exercises();
        GenerateServiceImpl giml = new GenerateServiceImpl();
        giml.generateExercises(e, 10);
        System.out.println(e);
    }


}
