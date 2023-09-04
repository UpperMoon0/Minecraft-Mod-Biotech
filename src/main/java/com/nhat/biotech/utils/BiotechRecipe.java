package com.nhat.biotech.utils;

public class BiotechRecipe {
    private BiotechRecipeEntry[] inputs;
    private boolean[] isInputConsumed;
    private BiotechRecipeEntry[] outputs;
    private int totalEnergy;
    public BiotechRecipe(BiotechRecipeEntry[] inputs, boolean[] isInputConsumed, BiotechRecipeEntry[] outputs, int totalEnergy) {
        this.inputs = inputs;
        this.isInputConsumed = isInputConsumed;
        this.outputs = outputs;
        this.totalEnergy = totalEnergy;
    }
    public BiotechRecipeEntry[] getInputs() {
        return inputs;
    }
    public boolean[] getIsInputConsumed() {
        return isInputConsumed;
    }
    public BiotechRecipeEntry[] getOutputs() {
        return outputs;
    }
    public int getTotalEnergy() {
        return totalEnergy;
    }
}
