package edu.upc.prop.teclat.domini.generatoralgorithms.hillclimbing;

import java.util.ArrayList;

public interface SuccessorFunction {
    public ArrayList<Object> getSuccessors(Object obj);
}