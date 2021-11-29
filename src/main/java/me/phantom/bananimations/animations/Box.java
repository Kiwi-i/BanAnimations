 package me.phantom.bananimations.animations;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import me.phantom.bananimations.AnimationType;
import me.phantom.bananimations.api.Animation;
import me.phantom.bananimations.utils.ArmorStandBuilder;
import me.phantom.bananimations.utils.Sounds;
import me.phantom.bananimations.utils.Task;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Box extends Animation {
   private ArmorStandBuilder ab;

   public Box(String animationName, Material type) {
      super(animationName);
      this.ab = (new ArmorStandBuilder(this.getPlugin(), (Location)null)).withNoGravity().withNoArms().withInvisible().withHelmet(new ItemStack(type));
   }

   public void playAnimation(CommandSender sender, Player target, AnimationType type, String reason) {
      super.freeze(target);
      Location targetLoc = target.getLocation().add(0.0D, -1.305D, 0.0D);
      List<ArmorStand> stands = new ArrayList<ArmorStand>();

      for(int i = 0; i < 3; ++i) {
         Location[] var8 = this.getSquareAroundLocation(targetLoc);
         int var9 = var8.length;

         for(int var10 = 0; var10 < var9; ++var10) {
            Location location = var8[var10];
            stands.add((ArmorStand)this.getPlugin().getMobUtils().setDefaultTags(this.ab.withLocation(location).spawn()));
         }

         targetLoc.add(0.0D, 0.61D, 0.0D);
      }

      if (this.getName().equals("freeze")) {
         super.playSound(target, sender, Sounds.BLOCK_GLASS_BREAK.get(), 0.5F, 3.0F);
      } else {
         super.playSound(target, sender, Sounds.BLOCK_ANVIL_LAND.get(), 0.5F, 3.0F);
      }

      Task.runTaskLater(() -> {
         stands.forEach((stand) -> {
            stand.remove();
         });
         super.finish(sender, target, type, reason);
      }, 5L, TimeUnit.SECONDS);
   }

   private Location[] getSquareAroundLocation(Location location) {
      Location[] square = new Location[4];
      World world = location.getWorld();
      double x = location.getX();
      double y = location.getY();
      double z = location.getZ();
      square[0] = new Location(world, x + 0.305D, y, z + 0.305D);
      square[1] = new Location(world, x - 0.305D, y, z + 0.305D);
      square[2] = new Location(world, x + 0.305D, y, z - 0.305D);
      square[3] = new Location(world, x - 0.305D, y, z - 0.305D);
      return square;
   }
}
