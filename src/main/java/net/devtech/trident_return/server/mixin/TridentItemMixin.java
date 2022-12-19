package net.devtech.trident_return.server.mixin;

import net.devtech.trident_return.server.TridentEntityDuck;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TridentItem;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

@Mixin(TridentItem.class)
public class TridentItemMixin {

    @Inject(method = "onStoppedUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void trident_return_onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks, CallbackInfo ci, PlayerEntity playerEntity, int i, TridentEntity tridentEntity) {
        ((TridentEntityDuck) tridentEntity).trident_return_setSlot(playerEntity.getActiveHand() == Hand.OFF_HAND ? PlayerInventory.OFF_HAND_SLOT : playerEntity.getInventory().selectedSlot);
    }
}
