class ank.gapi.controls.Label extends ank.gapi.core.UIBasicComponent
{
   static var CLASS_NAME = "Label";
   var _sTextfiledType = "dynamic";
   var _bMultiline = false;
   var _bWordWrap = false;
   var bDisplayDebug = false;
   function Label()
   {
      super();
   }
   function __set__html(bHTML)
   {
      this._bHTML = bHTML;
      this.setTextFieldProperties();
      return this.__get__html();
   }
   function __get__html()
   {
      return this._bHTML;
   }
   function __set__multiline(bMultiline)
   {
      this._bMultiline = bMultiline;
      this.setTextFieldProperties();
      return this.__get__multiline();
   }
   function __get__multiline()
   {
      return this._bMultiline;
   }
   function __set__wordWrap(bWordWrap)
   {
      this._bWordWrap = bWordWrap;
      this.setTextFieldProperties();
      return this.__get__wordWrap();
   }
   function __get__wordWrap()
   {
      return this._bWordWrap;
   }
   function __set__text(sText)
   {
      this._sText = sText;
      this._bSettingNewText = true;
      this.setTextFieldProperties();
      return this.__get__text();
   }
   function __get__text()
   {
      return this._tText.text != undefined?this._tText.text:this._sText;
   }
   function __get__textHeight()
   {
      return this._tText.textHeight;
   }
   function __get__textWidth()
   {
      return this._tText.textWidth;
   }
   function __set__textColor(nColor)
   {
      this._tText.textColor = nColor;
      return this.__get__textColor();
   }
   function setPreferedSize(oAutoSize)
   {
      this._tText.autoSize = oAutoSize;
      this.setSize(this.textWidth + 5,this.textHeight);
   }
   function init()
   {
      super.init(false,ank.gapi.controls.Label.CLASS_NAME);
      this._tfFormatter = new TextFormat();
   }
   function createChildren()
   {
      this.createEmptyMovieClip("_mcBackground",10);
      this.createEmptyMovieClip("_mcBorder",30);
      this.createTextField("_tText",35,0,1,this.__width,this.__height - 1);
      this._tText.background = false;
      this._tText.addListener(this);
      this._tText.onKillFocus = function()
      {
         this._parent.onKillFocus();
      };
      this._tText.onSetFocus = function()
      {
         this._parent.onSetFocus();
      };
      this.setTextFieldProperties();
   }
   function size()
   {
      super.size();
      this._tText._height = this.__height - 1;
      this._tDotText.removeTextField();
      this._mcDot.removeMovieClip();
      if(this._tText.textWidth > this.__width && this._sTextfiledType == "dynamic")
      {
         this.createTextField("_tDotText",20,0,1,this.__width,this.__height - 1);
         this._tDotText.selectable = false;
         this._tDotText.autoSize = "right";
         this._tDotText.embedFonts = this.getStyle().embedfonts;
         this._tDotText.text = "...";
         this._tDotText.setNewTextFormat(this._tfFormatter);
         this._tDotText.setTextFormat(this._tfFormatter);
         this._tText._width = this.__width - this._tDotText.textWidth;
         this.createEmptyMovieClip("_mcDot",30);
         ank.gapi.core.UIBasicComponent.drawRoundRect(this._mcDot,this.__width - this._tDotText.textWidth,0,this._tDotText.textWidth + 5,this.__height,0,0,0);
         this._mcDot.onRollOver = function()
         {
            this._parent.gapi.showTooltip(this._parent._sText,this._parent,0);
         };
         this._mcDot.onRollOut = function()
         {
            this._parent.gapi.hideTooltip();
         };
      }
      else
      {
         this._tText._width = this.__width;
      }
   }
   function draw()
   {
      var _loc2_ = this.getStyle();
      this._tfFormatter = this._tText.getTextFormat();
      this._tfFormatter.font = _loc2_.font;
      this._tfFormatter.align = _loc2_.align;
      this._tfFormatter.size = _loc2_.size;
      if(!this._bHTML)
      {
         this._tfFormatter.color = _loc2_.color;
      }
      this._tfFormatter.bold = _loc2_.bold;
      this._tfFormatter.italic = _loc2_.italic;
      this._tText.embedFonts = _loc2_.embedfonts;
      this._tText.antiAliasType = _loc2_.antialiastype;
      this.setTextFieldProperties();
      var _loc3_ = _loc2_.cornerradius;
      var _loc4_ = _loc2_.bordercolor;
      var _loc5_ = _loc2_.borderalpha == undefined?100:_loc2_.borderalpha;
      var _loc6_ = _loc2_.borderwidth == undefined?0:_loc2_.borderwidth;
      var _loc7_ = _loc2_.backgroundcolor;
      var _loc8_ = _loc2_.backgroundalpha == undefined?100:_loc2_.backgroundalpha;
      var _loc9_ = _loc2_.backgroundrotation == undefined?0:_loc2_.backgroundrotation;
      var _loc10_ = _loc2_.backgroundradient;
      var _loc11_ = _loc2_.backgroundratio;
      if(typeof _loc3_ == "object")
      {
         var _loc12_ = {tl:_loc3_.tl - _loc6_,tr:_loc3_.tr - _loc6_,br:_loc3_.bl - _loc6_,bl:_loc3_.bl - _loc6_};
      }
      else
      {
         _loc12_ = _loc3_ - _loc6_;
      }
      this._mcBorder.clear();
      this._mcBackground.clear();
      ank.gapi.core.UIBasicComponent.drawRoundBorder(this._mcBorder,0,0,this.__width,this.__height,_loc6_,_loc3_,_loc4_,_loc5_);
      ank.gapi.core.UIBasicComponent.drawRoundRect(this._mcBackground,_loc6_,_loc6_,this.__width - 2 * _loc6_,this.__height - 2 * _loc6_,_loc12_,_loc7_,_loc8_,_loc9_,_loc10_,_loc11_);
   }
   function setTextFieldProperties()
   {
      if(this._tText != undefined)
      {
         this._tText.wordWrap = this._bWordWrap;
         this._tText.multiline = this._bMultiline;
         this._tText.selectable = this._sTextfiledType == "input";
         this._tText.type = this._sTextfiledType;
         this._tText.html = this._bHTML;
         if(this._tfFormatter.font != undefined)
         {
            if(this._sText != undefined)
            {
               if(this._bHTML)
               {
                  this._tText.htmlText = this._sText;
               }
               else
               {
                  this._tText.text = this._sText;
               }
            }
            this._tText.setNewTextFormat(this._tfFormatter);
            this._tText.setTextFormat(this._tfFormatter);
         }
         if(this._tText.textWidth > this.__width && this._sTextfiledType == "dynamic")
         {
            this.size();
         }
         else
         {
            this._tDotText.removeTextField();
            this._mcDot.removeMovieClip();
         }
         this.onChanged();
      }
   }
   function onChanged()
   {
      this.dispatchEvent({type:"change"});
   }
   function onSetFocus()
   {
      getURL("FSCommand:" add "trapallkeys","false");
   }
   function onKillFocus()
   {
      getURL("FSCommand:" add "trapallkeys","true");
   }
}
