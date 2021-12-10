class ank.gapi.controls.StylizedRectangle extends ank.gapi.core.UIBasicComponent
{
   static var CLASS_NAME = "StylizedRectangle";
   function StylizedRectangle()
   {
      super();
   }
   function init()
   {
      super.init(false,ank.gapi.controls.StylizedRectangle.CLASS_NAME);
   }
   function createChildren()
   {
      this.createEmptyMovieClip("_mcBackground",10);
   }
   function size()
   {
      super.size();
      this.arrange();
   }
   function arrange()
   {
      if(this._bInitialized)
      {
         this.draw();
      }
   }
   function draw()
   {
      var _loc2_ = this.getStyle();
      var _loc3_ = _loc2_.cornerradius == undefined?0:_loc2_.cornerradius;
      var _loc4_ = _loc2_.bgcolor == undefined?16777215:_loc2_.bgcolor;
      var _loc5_ = _loc2_.alpha == undefined?100:_loc2_.alpha;
      var _loc6_ = _loc2_.rotation == undefined?0:_loc2_.rotation;
      this._mcBackground.clear();
      ank.gapi.core.UIBasicComponent.drawRoundRect(this._mcBackground,0,0,this.__width,this.__height,_loc3_,_loc4_,_loc5_,_loc6_);
      if(_loc2_.decorationstyle != undefined)
      {
         ank.gapi.core.UIBasicComponent.drawDecoration(this._mcBackground,ank.gapi.styles.StylesManager.getStyle(_loc2_.decorationstyle),0,0,this.__width,this.__height);
      }
   }
}
