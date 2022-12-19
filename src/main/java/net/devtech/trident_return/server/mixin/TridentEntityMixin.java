package net.devtech.trident_return.server.mixin;

import net.devtech.trident_return.server.TridentEntityDuck;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

@Mixin(TridentEntity.class)
public abstract class TridentEntityMixin extends PersistentProjectileEntity implements TridentEntityDuck {
    @Unique
    private int trident_return_slot = -1;

    protected TridentEntityMixin(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "tryPickup", at = @At("HEAD"), cancellable = true)
    private void trident_return_tryPickup(PlayerEntity player, CallbackInfoReturnable<Boolean> cir) {
        switch (this.pickupType) {
            case ALLOWED -> cir.setReturnValue(trident_return_returnOrInsertTrident(player));
            case CREATIVE_ONLY -> cir.setReturnValue(player.getAbilities().creativeMode);
            default -> cir.setReturnValue(this.isNoClip() && this.isOwner(player) && trident_return_returnOrInsertTrident(player));
        }
    }

    @Unique
    private boolean trident_return_returnOrInsertTrident(PlayerEntity player) {
        if (this.trident_return_slot == PlayerInventory.OFF_HAND_SLOT && player.getOffHandStack().isEmpty()) {
            player.setStackInHand(Hand.OFF_HAND, this.asItemStack());
            return true;
        }

        if (PlayerInventory.isValidHotbarIndex(this.trident_return_slot) && player.getInventory().getStack(this.trident_return_slot).isEmpty()) {
            return player.getInventory().insertStack(this.trident_return_slot, this.asItemStack());
        }

        return player.getInventory().insertStack(this.asItemStack());
    }

    @Override
    public void trident_return_setSlot(int slot) {
        this.trident_return_slot = slot;
    }
}
