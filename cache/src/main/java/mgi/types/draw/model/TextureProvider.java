package mgi.types.draw.model;

import mgi.tools.jagcached.ArchiveType;
import mgi.tools.jagcached.cache.Archive;
import mgi.tools.jagcached.cache.Cache;
import mgi.tools.jagcached.cache.File;
import mgi.tools.jagcached.cache.Group;
import mgi.types.draw.sprite.SpritePixels;
import mgi.utilities.Buffer;

public class TextureProvider implements TextureLoader {

   Texture[] textures;
   int capacity;
   int remaining = 0;
   double brightness = 1.0;
   int textureSize = 128;
   Archive archive;

   public TextureProvider(Cache cache, int var3, double var4, int var6) {
      this.archive = cache.getArchive(ArchiveType.SPRITES);
      this.capacity = var3;
      this.remaining = this.capacity;
      this.brightness = var4;
      this.textureSize = var6;
      final Group var1 = cache.getArchive(ArchiveType.TEXTURES).findGroupByID(0);
      this.textures = new Texture[var1.getHighestFileId()];
      for (File file : var1.getFiles()) {
         this.textures[file.getID()] = new Texture(file.getData());
      }
   }

   public void setBrightness(double var1) {
      this.brightness = var1;
      this.clear();
   }

   @Override
   public int[] getTexturePixels(int var1) {
      Texture var2 = this.textures[var1];
      if (var2 != null) {
         if (var2.pixels != null) {
            var2.isLoaded = true;
            return var2.pixels;
         }

         boolean var3 = var2.load(this.brightness, this.textureSize, this.archive);
         if (var3) {
            var2.isLoaded = true;
            return var2.pixels;
         }
      }

      return null;
   }

   @Override
   public int getAverageTextureRGB(int var1) {
      return this.textures[var1] != null ? this.textures[var1].averageRGB : 0;
   }

   @Override
   public boolean vmethod4859(int var1) {
      return this.textures[var1].field2453;
   }

   @Override
   public boolean isLowDetail(int var1) {
      return this.textureSize == 64;
   }

   public void clear() {
      for(int var1 = 0; var1 < this.textures.length; ++var1) {
         if (this.textures[var1] != null) {
            this.textures[var1].reset();
         }
      }
      this.remaining = this.capacity;
   }

   public void animate(int var1) {
      for(int var2 = 0; var2 < this.textures.length; ++var2) {
         Texture var3 = this.textures[var2];
         if (var3 != null && var3.animationDirection != 0 && var3.isLoaded) {
            var3.animate(var1);
            var3.isLoaded = false;
         }
      }

   }

}
