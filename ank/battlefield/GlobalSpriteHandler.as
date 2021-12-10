class ank.battlefield.GlobalSpriteHandler
{
   static var ACCESSORY_WEAPON = 0;
   static var ACCESSORY_HAT = 1;
   static var ACCESSORY_CAPE = 2;
   function GlobalSpriteHandler()
   {
      this.initialize();
   }
   function initialize()
   {
      this._oSprites = new Object();
      this._mclLoader = new MovieClipLoader();
      this._mclLoader.addListener(this);
      this._aFrameToGo = new Array();
   }
   function setAccessoriesRoot(path)
   {
      this._sAccessoriesPath = path;
   }
   function addSprite(mcSprite, oSpriteData)
   {
      this._oSprites[mcSprite._target] = {mc:mcSprite,data:oSpriteData};
      this.garbageCollector();
   }
   function setColors(mc, color1, color2, color3, color4)
   {
      var _loc7_ = this._oSprites[mc._target].data;
      if(color1 != -1)
      {
         _loc7_.color1 = color1;
      }
      if(color2 != -1)
      {
         _loc7_.color2 = color2;
      }
      if(color3 != -1)
      {
         _loc7_.color3 = color3;
      }
      if(color4 != -1)
      {
         _loc7_.color4 = color4;
      }
   }
   function setAccessories(mc, aAccessories)
   {
      var _loc4_ = this._oSprites[mc._target].data;
      if(aAccessories != undefined)
      {
         _loc4_.accessories = aAccessories;
      }
   }
   function applyColor(mc, nZone, isMount)
   {
      var _loc5_ = this.getSpriteData(mc);
      if(_loc5_ != undefined)
      {
         var _loc6_ = !(isMount && _loc5_.mount != undefined)?_loc5_["color" + nZone]:_loc5_.mount["color" + nZone];
         if(_loc6_ != undefined && _loc6_ != -1)
         {
            var _loc7_ = (_loc6_ & 16711680) >> 16;
            var _loc8_ = (_loc6_ & 65280) >> 8;
            var _loc9_ = _loc6_ & 255;
            var _loc10_ = new Color(mc);
            var _loc11_ = new Object();
            _loc11_ = {ra:"0",rb:_loc7_,ga:"0",gb:_loc8_,ba:"0",bb:_loc9_,aa:"100",ab:"0"};
            _loc10_.setTransform(_loc11_);
         }
      }
   }
   function applyAccessory(mc, accessoryID, side, mcToHide, bFix, sex)
   {
      if(bFix == undefined)
      {
         bFix = false;
      }
      var _loc8_ = this.getSpriteData(mc);
      if(_loc8_ != undefined)
      {
         var _loc9_ = _loc8_.accessories[accessoryID].gfx;
         mc.clip.removeMovieClip();
         if(bFix)
         {
            switch(_loc8_.direction)
            {
               case 3:
               case 4:
               case 7:
                  mc._x = - mc._x;
            }
         }
         if(_loc9_ != undefined)
         {
            if(mcToHide != undefined)
            {
               mcToHide.gotoAndStop(!(_loc9_.length == 0 || _loc9_ == "_")?2:1);
            }
            if(!ank.battlefield.Constants.USE_STREAMING_FILES || ank.battlefield.Constants.STREAMING_METHOD == "compact")
            {
               if(_loc9_.length == 5)
               {
                  if(_loc9_.substr(0,2) == "26")
                  {
                     _loc9_ = "16_" + _loc9_.substr(3,2);
                  }
               }
               else if(_loc9_.substr(0,2) == "26")
               {
                  _loc9_ = "16_" + _loc9_.substr(3,3);
               }
               mc.attachMovie(_loc9_,"clip",10);
               var _loc10_ = side + 1;
               if(_loc8_.accessories[accessoryID].frame != undefined)
               {
                  com.ankamagames.tools.Logger.out("WG Accessory " + accessoryID + " " + side + " " + _loc8_.accessories[accessoryID].frame,"ank.battlefield.GlobalSpriteHandler::applyAccessory","E:\\My_Work\\WLG1\\wakfugardiens\\client\\AnkCommon\\classes/ank/battlefield/GlobalSpriteHandler.as",168);
                  mc.clip.gotoAndStop(side + _loc8_.accessories[accessoryID].frame);
               }
               else if(sex != undefined)
               {
                  mc.clip.gotoAndStop(side + sex);
               }
               else
               {
                  mc.clip.gotoAndStop(side);
               }
            }
            else
            {
               var _loc11_ = _loc9_.split("_");
               if(_loc11_[0] == undefined || (_global.isNaN(Number(_loc11_[0])) || (_loc11_[1] == undefined || _global.isNaN(Number(_loc11_[1])))))
               {
                  return undefined;
               }
               var _loc12_ = mc.createEmptyMovieClip("clip",10);
               if(_loc8_.skin !== undefined)
               {
                  this._aFrameToGo[_loc12_] = side + _loc8_.skin;
               }
               else
               {
                  this._aFrameToGo[_loc12_] = side;
               }
               this._mclLoader.loadClip(this._sAccessoriesPath + _loc11_.join("/") + ".swf",_loc12_);
            }
         }
      }
   }
   function applyAnim(mc, sAnim)
   {
      var _loc4_ = this.getSpriteData(mc);
      if(_loc4_ != undefined)
      {
         if(_loc4_.bAnimLoop)
         {
            _loc4_.mc.saveLastAnimation(_loc4_.animation);
         }
         else
         {
            _loc4_.mc.setAnim(sAnim);
         }
      }
   }
   function applyEnd(mc)
   {
      var _loc3_ = this.getSpriteData(mc);
      if(_loc3_ != undefined)
      {
         if(!_loc3_.bAnimLoop)
         {
            _loc3_.sequencer.onActionEnd();
         }
      }
   }
   function applySprite(mc)
   {
      var _loc3_ = this.getSpriteData(mc);
      switch(_loc3_.direction)
      {
         case 0:
         case 4:
            mc.attachMovie(_loc3_.animation + "S","clip",1);
            break;
         case 1:
         case 3:
            mc.attachMovie(_loc3_.animation + "R","clip",1);
            break;
         case 2:
            mc.attachMovie(_loc3_.animation + "F","clip",1);
            break;
         case 5:
         case 7:
            mc.attachMovie(_loc3_.animation + "L","clip",1);
            break;
         case 6:
            mc.attachMovie(_loc3_.animation + "B","clip",1);
      }
   }
   function registerCarried(mc)
   {
      var _loc3_ = this.getSpriteData(mc);
      _loc3_.mc.mcCarried = mc;
   }
   function registerChevauchor(mc)
   {
      var _loc3_ = this.getSpriteData(mc);
      _loc3_.mc.mcChevauchorPos = mc;
      _loc3_.mc.updateChevauchorPosition();
   }
   function getSpriteData(mc)
   {
      var _loc3_ = mc._target;
      for(var name in this._oSprites)
      {
         if(_loc3_.substring(0,name.length) == name)
         {
            if(_loc3_.charAt(name.length) != "/")
            {
               continue;
            }
            if(this._oSprites[name] != undefined)
            {
               return this._oSprites[name].data;
            }
         }
      }
   }
   function garbageCollector(Void)
   {
      for(var o in this._oSprites)
      {
         if(this._oSprites[o].mc._target == undefined)
         {
            delete this._oSprites.o;
         }
      }
   }
   function recursiveGotoAndStop(mc, frame)
   {
      mc.stop();
      mc.gotoAndStop(frame);
      for(var i in mc)
      {
         if(mc[i] instanceof MovieClip)
         {
            this.recursiveGotoAndStop(mc[i],frame);
         }
      }
   }
   function onLoadInit(mc)
   {
      this.recursiveGotoAndStop(mc,this._aFrameToGo[mc]);
      delete this._aFrameToGo.mc;
   }
}
