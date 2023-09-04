package com.nhat.biotech.utils;

import com.nhat.biotech.items.IModItems;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.List;

public abstract class BreederRecipes {
    private static final List<BiotechRecipe> recipeList = new ArrayList<>();
    static {
        recipeList.add(new BiotechRecipe(
                new BiotechRecipeEntry[]{
                        new BiotechRecipeEntry(IModItems.COW.get(), 2),
                        new BiotechRecipeEntry(Items.WHEAT, 2)
                },
                new boolean[]{false, true},
                new BiotechRecipeEntry[]{
                        new BiotechRecipeEntry(IModItems.BABY_COW.get(), 1)
                },
                10000
        ));
        recipeList.add(new BiotechRecipe(
                new BiotechRecipeEntry[]{
                        new BiotechRecipeEntry(IModItems.SHEEP.get(), 2),
                        new BiotechRecipeEntry(Items.WHEAT, 2)
                },
                new boolean[]{false, true},
                new BiotechRecipeEntry[]{
                        new BiotechRecipeEntry(IModItems.BABY_SHEEP.get(), 1)
                },
                10000
        ));
        recipeList.add(new BiotechRecipe(
                new BiotechRecipeEntry[]{
                        new BiotechRecipeEntry(IModItems.CHICKEN.get(), 2),
                        new BiotechRecipeEntry(Items.WHEAT_SEEDS, 2)
                },
                new boolean[]{false, true},
                new BiotechRecipeEntry[]{
                        new BiotechRecipeEntry(IModItems.BABY_CHICKEN.get(), 1)
                },
                10000
        ));
        recipeList.add(new BiotechRecipe(
                new BiotechRecipeEntry[]{
                        new BiotechRecipeEntry(IModItems.CHICKEN.get(), 2),
                        new BiotechRecipeEntry(Items.MELON_SEEDS, 2)
                },
                new boolean[]{false, true},
                new BiotechRecipeEntry[]{
                        new BiotechRecipeEntry(IModItems.BABY_CHICKEN.get(), 1)
                },
                10000
        ));
        recipeList.add(new BiotechRecipe(
                new BiotechRecipeEntry[]{
                        new BiotechRecipeEntry(IModItems.CHICKEN.get(), 2),
                        new BiotechRecipeEntry(Items.PUMPKIN_SEEDS, 2)
                },
                new boolean[]{false, true},
                new BiotechRecipeEntry[]{
                        new BiotechRecipeEntry(IModItems.BABY_CHICKEN.get(), 1)
                },
                10000
        ));
        recipeList.add(new BiotechRecipe(
                new BiotechRecipeEntry[]{
                        new BiotechRecipeEntry(IModItems.CHICKEN.get(), 2),
                        new BiotechRecipeEntry(Items.BEETROOT_SEEDS, 2)
                },
                new boolean[]{false, true},
                new BiotechRecipeEntry[]{
                        new BiotechRecipeEntry(IModItems.BABY_CHICKEN.get(), 1)
                },
                10000
        ));
        recipeList.add(new BiotechRecipe(
                new BiotechRecipeEntry[]{
                        new BiotechRecipeEntry(IModItems.CHICKEN.get(), 2),
                        new BiotechRecipeEntry(Items.TORCHFLOWER_SEEDS, 2)
                },
                new boolean[]{false, true},
                new BiotechRecipeEntry[]{
                        new BiotechRecipeEntry(IModItems.BABY_CHICKEN.get(), 1)
                },
                10000
        ));
        recipeList.add(new BiotechRecipe(
                new BiotechRecipeEntry[]{
                        new BiotechRecipeEntry(IModItems.PIG.get(), 2),
                        new BiotechRecipeEntry(Items.CARROT, 2)
                },
                new boolean[]{false, true},
                new BiotechRecipeEntry[]{
                        new BiotechRecipeEntry(IModItems.BABY_PIG.get(), 1)
                },
                10000
        ));
        recipeList.add(new BiotechRecipe(
                new BiotechRecipeEntry[]{
                        new BiotechRecipeEntry(IModItems.PIG.get(), 2),
                        new BiotechRecipeEntry(Items.POTATO, 2)
                },
                new boolean[]{false, true},
                new BiotechRecipeEntry[]{
                        new BiotechRecipeEntry(IModItems.BABY_PIG.get(), 1)
                },
                10000
        ));
        recipeList.add(new BiotechRecipe(
                new BiotechRecipeEntry[]{
                        new BiotechRecipeEntry(IModItems.PIG.get(), 2),
                        new BiotechRecipeEntry(Items.BEETROOT, 2)
                },
                new boolean[]{false, true},
                new BiotechRecipeEntry[]{
                        new BiotechRecipeEntry(IModItems.BABY_PIG.get(), 1)
                },
                10000
        ));
        recipeList.add(new BiotechRecipe(
                new BiotechRecipeEntry[]{
                        new BiotechRecipeEntry(IModItems.RABBIT.get(), 2),
                        new BiotechRecipeEntry(Items.CARROT, 2)
                },
                new boolean[]{false, true},
                new BiotechRecipeEntry[]{
                        new BiotechRecipeEntry(IModItems.BABY_RABBIT.get(), 1)
                },
                10000
        ));
        recipeList.add(new BiotechRecipe(
                new BiotechRecipeEntry[]{
                        new BiotechRecipeEntry(IModItems.RABBIT.get(), 2),
                        new BiotechRecipeEntry(Items.DANDELION, 2)
                },
                new boolean[]{false, true},
                new BiotechRecipeEntry[]{
                        new BiotechRecipeEntry(IModItems.BABY_RABBIT.get(), 1)
                },
                10000
        ));
        recipeList.add(new BiotechRecipe(
                new BiotechRecipeEntry[]{
                        new BiotechRecipeEntry(IModItems.RABBIT.get(), 2),
                        new BiotechRecipeEntry(Items.GOLDEN_CARROT, 2)
                },
                new boolean[]{false, true},
                new BiotechRecipeEntry[]{
                        new BiotechRecipeEntry(IModItems.BABY_RABBIT.get(), 1)
                },
                10000
        ));
    }
    public static List<BiotechRecipe> getRecipeList() {
        return recipeList;
    }
}
