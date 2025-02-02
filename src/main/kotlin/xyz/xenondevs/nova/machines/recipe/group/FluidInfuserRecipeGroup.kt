package xyz.xenondevs.nova.machines.recipe.group

import de.studiocode.invui.gui.GUI
import de.studiocode.invui.gui.builder.GUIBuilder
import de.studiocode.invui.gui.builder.guitype.GUIType
import de.studiocode.invui.item.builder.ItemBuilder
import net.md_5.bungee.api.chat.TranslatableComponent
import xyz.xenondevs.nova.data.config.NovaConfig
import xyz.xenondevs.nova.data.recipe.RecipeContainer
import xyz.xenondevs.nova.machines.recipe.FluidInfuserRecipe
import xyz.xenondevs.nova.machines.registry.Blocks
import xyz.xenondevs.nova.machines.registry.GUIMaterials
import xyz.xenondevs.nova.machines.registry.GUITextures
import xyz.xenondevs.nova.material.CoreGUIMaterial
import xyz.xenondevs.nova.ui.StaticFluidBar
import xyz.xenondevs.nova.ui.menu.item.recipes.createRecipeChoiceItem
import xyz.xenondevs.nova.ui.menu.item.recipes.group.RecipeGroup

private val FLUID_CAPACITY = NovaConfig[Blocks.FLUID_INFUSER].getLong("fluid_capacity")

object FluidInfuserRecipeGroup : RecipeGroup() {
    
    override val texture = GUITextures.RECIPE_FLUID_INFUSER
    override val icon = Blocks.FLUID_INFUSER.basicClientsideProvider
    override val priority = 6
    
    override fun createGUI(container: RecipeContainer): GUI {
        val recipe = container.recipe as FluidInfuserRecipe
        
        val progressItem: ItemBuilder
        val translate: String
        if (recipe.mode == FluidInfuserRecipe.InfuserMode.INSERT) {
            progressItem = GUIMaterials.TP_FLUID_PROGRESS_LEFT_RIGHT.createItemBuilder()
            translate = "menu.machines.recipe.insert_fluid"
        } else {
            progressItem = GUIMaterials.TP_FLUID_PROGRESS_RIGHT_LEFT.createItemBuilder()
            translate = "menu.machines.recipe.extract_fluid"
        }
        
        progressItem.setDisplayName(TranslatableComponent(
            translate,
            recipe.fluidAmount,
            TranslatableComponent(recipe.fluidType.localizedName)
        ))
        
        return GUIBuilder(GUIType.NORMAL)
            .setStructure(
                ". f . t . . . . .",
                ". f p i . . . r .",
                ". f . . . . . . .")
            .addIngredient('i', createRecipeChoiceItem(recipe.input))
            .addIngredient('r', createRecipeChoiceItem(listOf(recipe.result)))
            .addIngredient('p', progressItem)
            .addIngredient('f', StaticFluidBar(recipe.fluidType, recipe.fluidAmount, FLUID_CAPACITY, 3))
            .addIngredient('t', CoreGUIMaterial.TP_STOPWATCH
                .createClientsideItemBuilder()
                .setDisplayName(TranslatableComponent("menu.nova.recipe.time", recipe.time / 20.0))
            )
            .build()
    }
    
}