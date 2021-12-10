class ank.gapi.controls.TextArea extends ank.gapi.core.UIBasicComponent
{
   static var CLASS_NAME = "TextArea";
   var _bEditable = true;
   var _bSelectable = true;
   var _bAutoHeight = false;
   var _bWordWrap = true;
   var _bScrollBarRight = true;
   var _bHTML = false;
   var _nScrollBarMargin = 0;
   function TextArea()
   {
      super();
   }
   function __set__border(bBorder)
   {
      return this.__get__border();
   }
   function __get__border()
   {
      return true;
   }
   function __set__url(sURL)
   {
      this._sURL = sURL;
      if(this._sURL != "")
      {
         this.loadText();
      }
      return this.__get__url();
   }
   function __set__editable(bEditable)
   {
      this._bEditable = bEditable;
      if(this._bInitialized)
      {
         this.addToQueue({object:this,method:this.setTextFieldProperties});
      }
      return this.__get__editable();
   }
   function __get__editable()
   {
      return this._bEditable;
   }
   function __set__autoHeight(bAutoHeight)
   {
      this._bAutoHeight = bAutoHeight;
      if(this._bInitialized)
      {
         this.addToQueue({object:this,method:this.setTextFieldProperties});
      }
      return this.__get__autoHeight();
   }
   function __get__autoHeight()
   {
      return this._bAutoHeight;
   }
   function __set__selectable(bSelectable)
   {
      this._bSelectable = bSelectable;
      if(this._bInitialized)
      {
         this.addToQueue({object:this,method:this.setTextFieldProperties});
      }
      return this.__get__selectable();
   }
   function __get__selectable()
   {
      return this._bSelectable;
   }
   function __set__wordWrap(bWordWrap)
   {
      this._bWordWrap = bWordWrap;
      if(this._bInitialized)
      {
         this.addToQueue({object:this,method:this.setTextFieldProperties});
      }
      return this.__get__wordWrap();
   }
   function __get__wordWrap()
   {
      return this._bWordWrap;
   }
   function __set__html(bHTML)
   {
      this._bHTML = bHTML;
      if(this._bInitialized)
      {
         this.addToQueue({object:this,method:this.setTextFieldProperties});
      }
      return this.__get__html();
   }
   function __get__html()
   {
      return this._bHTML;
   }
   function __set__text(sText)
   {
      this._sText = sText;
      this._bSettingNewText = true;
      this.addToQueue({object:this,method:this.setTextFieldProperties});
      return this.__get__text();
   }
   function __get__text()
   {
      return this._tText.text;
   }
   function __set__scrollBarRight(bScrollBarRight)
   {
      this._bScrollBarRight = bScrollBarRight;
      return this.__get__scrollBarRight();
   }
   function __get__scrollBarRight()
   {
      return this._bScrollBarRight;
   }
   function __set__scrollBarMargin(nScrollBarMargin)
   {
      this._nScrollBarMargin = nScrollBarMargin;
      return this.__get__scrollBarMargin();
   }
   function __get__scrollBarMargin()
   {
      return this._nScrollBarMargin;
   }
   function __set__styleSheet(sCSS)
   {
      if(sCSS != "")
      {
         var _owner = this;
         this._cssStyles = new TextField.StyleSheet();
         this._cssStyles.load(sCSS);
         this._cssStyles.onLoad = function(bSuccess)
         {
            if(_owner._tText != undefined)
            {
               _owner.addToQueue({object:_owner,method:_owner.setTextFieldProperties});
            }
         };
      }
      else
      {
         this._cssStyles = undefined;
         this._tText.styleSheet = null;
      }
      return this.__get__styleSheet();
   }
   function __set__scrollPosition(nScrollPosition)
   {
      this._tText.scroll = nScrollPosition;
      return this.__get__scrollPosition();
   }
   function __get__scrollPosition()
   {
      return this._tText.scroll;
   }
   function __set__maxscroll(nMaxScroll)
   {
      this._tText.maxscroll = nMaxScroll;
      return this.__get__maxscroll();
   }
   function __get__maxscroll()
   {
      return this._tText.maxscroll;
   }
   function __set__maxChars(nMaxChars)
   {
      this._tText.maxChars = nMaxChars;
      return this.__get__maxChars();
   }
   function __get__maxChars()
   {
      return this._tText.maxChars;
   }
   function __get__textHeight()
   {
      return this._tText.textHeight;
   }
   function init()
   {
      super.init(false,ank.gapi.controls.TextArea.CLASS_NAME);
      if(this._sURL != undefined)
      {
         this.loadText();
      }
      this._tfFormatter = new TextFormat();
   }
   function createChildren()
   {
      this.createEmptyMovieClip("_mcBackground",10);
      this.createEmptyMovieClip("_mcBorder",30);
      this.createTextField("_tText",20,0,0,this.__width - 2,this.__height - 2);
      this._tText._x = 1;
      this._tText._y = 1;
      this._tText.background = false;
      this._tText.addListener(this);
      this._tText.onSetFocus = function()
      {
         this._parent.onSetFocus();
      };
      this._tText.onKillFocus = function()
      {
         this._parent.onKillFocus();
      };
      ank.utils.MouseEvents.addListener(this);
   }
   function size()
   {
      super.size();
      this.arrange();
   }
   function arrange()
   {
      this._sbVertical.setSize(this.__height);
      this._tText._height = this.__height;
      this._tText._width = this.__width;
   }
   function draw()
   {
      var _loc2_ = this.getStyle();
      this._tfFormatter = new TextFormat();
      this._tfFormatter.font = _loc2_.font;
      this._tfFormatter.align = _loc2_.align;
      this._tfFormatter.size = _loc2_.size;
      this._tfFormatter.color = _loc2_.color;
      this._tfFormatter.bold = _loc2_.bold;
      this._tfFormatter.italic = _loc2_.italic;
      this._tText.embedFonts = _loc2_.embedfonts;
      this._tText.antiAliasType = _loc2_.antialiastype;
      this._sbVertical.styleName = _loc2_.scrollbarstyle;
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
   function loadText()
   {
      if(this._sURL == undefined || this._sURL == "")
      {
         return undefined;
      }
      this._lvText = new LoadVars();
      this._lvText.parent = this;
      this._lvText.onData = function(sData)
      {
         this.parent.text = sData;
      };
      this._lvText.load(this._sURL);
   }
   function setTextFieldProperties()
   {
      if(this._tText != undefined)
      {
         if(this._bAutoHeight)
         {
            this._tText.autoSize = "left";
         }
         this._tText.wordWrap = !this._bWordWrap?false:true;
         this._tText.multiline = true;
         this._tText.selectable = this._bSelectable;
         this._tText.type = !this._bEditable?"dynamic":"input";
         this._tText.html = this._bHTML;
         if(this._cssStyles != undefined)
         {
            this._tText.styleSheet = this._cssStyles;
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
         }
         else if(this._tfFormatter.font != undefined)
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
         this.onChanged();
      }
   }
   function addScrollBar()
   {
      if(this._sbVertical == undefined)
      {
         this.attachMovie("ScrollBar","_sbVertical",25,{styleName:this.getStyle().scrollbarstyle});
         this._sbVertical.setSize(this.__height - 2);
         this._sbVertical._y = 1;
         this._sbVertical._x = !this._bScrollBarRight?0:this.__width - this._sbVertical._width - 3;
         this._tText._width = this.__width - this._sbVertical._width - 3 - this._nScrollBarMargin;
         this._tText._x = !this._bScrollBarRight?this._sbVertical._width + this._nScrollBarMargin:0;
         this._sbVertical.addEventListener("scroll",this);
      }
      var _loc2_ = this._tText.textHeight;
      var _loc3_ = 0.9 * this._tText._height / _loc2_ * this._tText.maxscroll;
      this._sbVertical.setScrollProperties(_loc3_,0,this._tText.maxscroll);
      this._sbVertical.scrollPosition = this._tText.scroll;
      if(this._bSettingNewText)
      {
         this._sbVertical.scrollPosition = 0;
         this._bSettingNewText = false;
      }
   }
   function removeScrollBar()
   {
      if(this._sbVertical != undefined)
      {
         this._sbVertical.removeMovieClip();
         this._tText._width = this.__width;
      }
   }
   function onChanged()
   {
      if(this._tText.textHeight >= this._tText._height || this._cssStyles != undefined && this._tText.textHeight + 5 >= this._tText._height)
      {
         this.addScrollBar();
      }
      else
      {
         this.removeScrollBar();
      }
      if(this._bAutoHeight && this._tText.textHeight != this.__height)
      {
         this.setSize(this.__width,this._tText.textHeight);
         this.dispatchEvent({type:"resize"});
      }
      this.dispatchEvent({type:"change"});
   }
   function scroll(oEvent)
   {
      if(oEvent.target == this._sbVertical)
      {
         this._tText.scroll = oEvent.target.scrollPosition;
      }
   }
   function onMouseWheel(nDelta, mc)
   {
      if(String(mc._target).indexOf(this._target) != -1)
      {
         this._sbVertical.scrollPosition = this._sbVertical.scrollPosition - nDelta;
      }
   }
   function onHref(sParams)
   {
      this.dispatchEvent({type:"href",params:sParams});
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
