package me.phantom.bananimations.animations;

import java.util.List;
import me.phantom.bananimations.AnimationType;
import me.phantom.bananimations.utils.RepeatingTaskHelper;
import me.phantom.bananimations.utils.Sounds;
import me.phantom.bananimations.utils.Utils;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

class YinYang2 extends BukkitRunnable {
   double radPerSec;
   double yDif;
   boolean up;
   final RepeatingTaskHelper taskHelper;
   final List<Item> items;
   final ArmorStand[] stands;
   final World world;
   final Location targetLocation;
   final CommandSender sender;
   final Player target;
   final AnimationType type;
   final String reason;
   final YinYang animation;

   YinYang2(YinYang animation, RepeatingTaskHelper taskHelper, List<Item> items, ArmorStand[] stands, World world, Location location, CommandSender sender, Player player, AnimationType type, String reason) {
      this.animation = animation;
      this.taskHelper = taskHelper;
      this.items = items;
      this.stands = stands;
      this.world = world;
      this.targetLocation = location;
      this.sender = sender;
      this.target = player;
      this.type = type;
      this.reason = reason;
      this.radPerSec = Math.toRadians(10);
      this.yDif = 0.0D;
      this.up = true;
   }

   public void run() {
      int var3;
      if(this.taskHelper.getCounter() == 150) {
    	 this.world.playEffect(this.targetLocation, Effect.valueOf("SMOKE"), 1);
         this.world.playSound(this.targetLocation, Sounds.ENTITY_GENERIC_EXPLODE.get(), 1.0F, 1.0F);
         YinYang.finish(this.animation, this.sender, this.target, this.type, this.reason);
    	  }
         if(this.taskHelper.getCounter() == 160) {
         this.items.forEach((item) -> {
            item.remove();
         });
         ArmorStand[] stands = this.stands;

         for(int target = 0; target<stands.length;target++) {
            ArmorStand stand = stands[target];
            stand.remove();
         }
      } else {
         int count = 0;
         ArmorStand[] stands = this.stands;

         for(int target = 0; target < stands.length; ++target) {
            ArmorStand stand = stands[target];
            Location nextPoint = Utils.getLocationAroundCircle(this.targetLocation, YinYang.getRadius(this.animation), this.radPerSec * (float)this.taskHelper.getCounter() + (float)count);
            if (count == 0) {
               stand.teleport(new Location(this.world, nextPoint.getX(), nextPoint.getY() + this.yDif, nextPoint.getZ()));
            } else {
               stand.teleport(new Location(this.world, nextPoint.getX(), nextPoint.getY() - this.yDif, nextPoint.getZ()));
            }

            count += 3;
         }

         this.radPerSec = this.radPerSec + Math.toRadians(0.05);
         if (this.yDif >= 0.8D) {
            this.up = false;
         } else if (this.yDif <= -0.8D) {
            this.up = true;
         }

         if (this.up) {
            this.yDif += 0.02D;
         } else {
            this.yDif -= 0.02D;
         }

         Item item;
         Location loc = new Location(this.world,this.target.getEyeLocation().getX()-0.5F,this.target.getEyeLocation().getY(),this.target.getEyeLocation().getZ()-0.5F);
         if (this.animation.getRandom().nextInt(2) == 0) {

            item = this.target.getWorld().dropItemNaturally(loc, YinYang.getBlackWool(this.animation));
         } else {
            item = this.target.getWorld().dropItemNaturally(loc, YinYang.getWhiteWool(this.animation));
         }

         Utils.setLore(item.getItemStack(), this.animation.getRandom().nextDouble() + "");
         item.setPickupDelay(1000);
         item.setVelocity(new Vector(this.animation.getRandom().nextDouble() * 0.2D - 0.1D, 0.8D, this.animation.getRandom().nextDouble() * 0.2D - 0.1D));
         this.items.add(item);
         if (this.items.size() % 40 == 0) {
            this.items.get(0).remove();
            this.items.remove(0);
         }
         this.taskHelper.increment();
      }

   }
}