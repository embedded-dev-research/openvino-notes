package com.itlab.ai;

import org.openvino.java.OpenVINO;
import org.openvino.java.core.Core;
import org.openvino.java.core.Model;
import org.openvino.java.core.CompiledModel;
import org.openvino.java.core.InferRequest;
import org.openvino.java.core.Tensor;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;
import java.util.Arrays;

public class CotypeTag {
    
    private static Core core;
    private static CompiledModel compiledModel;
    private static String inputName;
    private static String outputName;
    private static boolean initialized = false;
    
    public static synchronized void init(String modelPath) {
        if (initialized) return;
        
        OpenVINO.load();
        core = new Core();
        Model model = core.readModel(modelPath);
        compiledModel = core.compileModel(model, "CPU");
        
        inputName = ((org.openvino.java.core.Input) compiledModel.inputs().get(0)).getAnyName();
        outputName = ((org.openvino.java.core.Output) compiledModel.outputs().get(0)).getAnyName();
        
        initialized = true;
    }
    
    public static String tag(String text) {
        if (!initialized) return "0";
        String prompt = "Определи тему текста. Ответь одним словом:\n\n" + text + "\n\nТема:";
        return runInference(prompt);
    }
    
    private static String runInference(String prompt) {
        try {
            int[] tokens = new int[prompt.length() + 2];
            tokens[0] = 1;
            for (int i = 0; i < prompt.length(); i++) {
                tokens[i + 1] = (int) prompt.charAt(i) + 1000;
            }
            tokens[prompt.length() + 1] = 2;
            
            int seqLen = Math.min(tokens.length, 128);
            float[] inputData = new float[seqLen];
            for (int i = 0; i < seqLen; i++) inputData[i] = tokens[i];
            
            float[] mask = new float[seqLen];
            Arrays.fill(mask, 1.0f);
            
            float[] positions = new float[seqLen];
            for (int i = 0; i < seqLen; i++) positions[i] = i;
            
            float[] beam = {0.0f};
            
            InferRequest request = compiledModel.createInferRequest();
            request.getTensor(inputName).setData(inputData);
            request.getTensor("attention_mask").setData(mask);
            request.getTensor("position_ids").setData(positions);
            request.getTensor("beam_idx").setData(beam);
            
            request.infer();
            
            Tensor outputTensor = request.getTensor(outputName);
            java.lang.reflect.Method getData = Tensor.class.getDeclaredMethod("getData");
            getData.setAccessible(true);
            Object obj = getData.invoke(outputTensor);
            
            if (obj instanceof PointerByReference) {
                Pointer ptr = ((PointerByReference) obj).getValue();
                if (ptr != null) {
                    float[] outputData = new float[5000];
                    ptr.read(0, outputData, 0, 5000);
                    
                    int maxIdx = 0;
                    for (int i = 0; i < outputData.length; i++) {
                        if (outputData[i] > outputData[maxIdx]) maxIdx = i;
                    }
                    return String.valueOf(maxIdx);
                }
            }
            return "0";
        } catch (Exception e) {
            return "0";
        }
    }
}
