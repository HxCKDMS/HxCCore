package hxckdms.hxccore.commands;

import hxckdms.hxccore.api.command.AbstractSubCommand;
import hxckdms.hxccore.api.command.HxCCommand;
import hxckdms.hxccore.api.command.TranslatedCommandException;
import hxckdms.hxccore.configs.KitConfiguration;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.potion.Potion;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@HxCCommand
public class CommandKit extends AbstractSubCommand<CommandHxC> {
    @Override
    public String getCommandName() {
        return "kit";
    }

    @Override
    public void execute(ICommandSender sender, LinkedList<String> args) throws CommandException {
        if (!(sender instanceof EntityPlayerMP)) throw new TranslatedCommandException(sender, "commands.exception.playersOnly");
        EntityPlayerMP player = (EntityPlayerMP) sender;

        for (Map.Entry<String, KitConfiguration.DummyItem> entry : KitConfiguration.kitTest.entrySet()) {
            ItemStack stack = new ItemStack(Item.getByNameOrId(entry.getKey()), entry.getValue().amount, entry.getValue().metadata);
            stack.setStackDisplayName(entry.getValue().itemName);
            NBTTagCompound tagCompound = entry.getValue().nbtData;

            NBTTagList enchantTagList = tagCompound.getTagList("ench", 10);
            boolean hasAdded = false;
            for (Map.Entry<String, Integer> enchantEntry : entry.getValue().enchantments.entrySet()) {
                Enchantment enchant;
                if ((enchant = Enchantment.getEnchantmentByLocation(enchantEntry.getKey())) != null) {
                    Enchantment.getEnchantmentID(enchant);

                    NBTTagCompound EnchantTagCompound = new NBTTagCompound();
                    EnchantTagCompound.setShort("id", (short) Enchantment.getEnchantmentID(enchant));
                    EnchantTagCompound.setShort("lvl", enchantEntry.getValue().shortValue());
                    enchantTagList.appendTag(EnchantTagCompound);
                    hasAdded = true;
                }
            }
            if (hasAdded) tagCompound.setTag("ench", enchantTagList);
            hasAdded = false;

            NBTTagList customPotionList = tagCompound.getTagList("CustomPotionEffects", 10);

            for (KitConfiguration.DummyPotionEffect customPotionEffect : entry.getValue().customPotionEffects) {
                Potion potion = Potion.getPotionFromResourceLocation(customPotionEffect.name);
                if (potion == null) continue;
                NBTTagCompound customPotionTag = new NBTTagCompound();
                customPotionTag.setByte("Id", (byte) Potion.getIdFromPotion(potion));
                customPotionTag.setByte("Amplifier", customPotionEffect.amplifier);
                customPotionTag.setInteger("Duration", customPotionEffect.duration);
                customPotionTag.setBoolean("Ambient", customPotionEffect.ambient);
                customPotionTag.setBoolean("ShowParticles", customPotionEffect.showParticles);
                customPotionList.appendTag(customPotionTag);
                hasAdded = true;
            }

            if (hasAdded) tagCompound.setTag("CustomPotionEffects", customPotionList);

            NBTTagCompound displayCompound = tagCompound.getCompoundTag("display");

            NBTTagList toolTips = new NBTTagList();
            entry.getValue().lore.forEach(System.out::println);
            entry.getValue().lore.stream().map(NBTTagString::new).forEachOrdered(toolTips::appendTag);
            displayCompound.setTag("Lore", toolTips);
            tagCompound.setTag("display", displayCompound);
            stack.setTagCompound(tagCompound);
            player.inventory.addItemStackToInventory(stack);
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, LinkedList<String> args, @Nullable BlockPos pos) {
        return Collections.emptyList();
    }
}
