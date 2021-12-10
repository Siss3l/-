class ank.utils.SharedObjectFix extends Object
{
   static var _soName = "WRAP";
   static var _soDefaultRequestedSize = 2097152;
   static var _so = null;
   static var _oLocalCache = new Object();
   function SharedObjectFix(name)
   {
      super();
      this._name = name;
      if(ank.utils.SharedObjectFix._so == null)
      {
         ank.utils.SharedObjectFix._so = SharedObject.getLocal(ank.utils.SharedObjectFix._soName);
         if(ank.utils.SharedObjectFix._so.data._Data == undefined)
         {
            ank.utils.SharedObjectFix._so.data._Data = new Object();
         }
         ank.utils.SharedObjectFix._so.onStatus = this._onStatus;
         ank.utils.SharedObjectFix._so.onSync = this._onSync;
      }
      if(ank.utils.SharedObjectFix._so.data._Data[this._name] == undefined)
      {
         ank.utils.SharedObjectFix._so.data._Data[this._name] = new Object();
      }
      this.data = ank.utils.SharedObjectFix._so.data._Data[this._name];
   }
   function _onStatus(infoObject)
   {
      com.ankamagames.tools.Logger.out("WG SharedObjectFix.as:54 onStatus","ank.utils.SharedObjectFix::_onStatus","E:\\My_Work\\WLG1\\wakfugardiens\\client\\AnkCommon\\classes/ank/utils/SharedObjectFix.as",59);
      for(var index in ank.utils.SharedObjectFix._oLocalCache)
      {
         if(ank.utils.SharedObjectFix._oLocalCache[index].onStatus)
         {
            ank.utils.SharedObjectFix._oLocalCache[index].onStatus(infoObject);
         }
      }
   }
   function _onSync(objArray)
   {
      com.ankamagames.tools.Logger.out("WG SharedObjectFix.as:61 onSync","ank.utils.SharedObjectFix::_onSync","E:\\My_Work\\WLG1\\wakfugardiens\\client\\AnkCommon\\classes/ank/utils/SharedObjectFix.as",66);
      for(var index in ank.utils.SharedObjectFix._oLocalCache)
      {
         if(ank.utils.SharedObjectFix._oLocalCache[index].onSync)
         {
            ank.utils.SharedObjectFix._oLocalCache[index].onSync(objArray);
         }
      }
   }
   function clear()
   {
      this.data = new Object();
      this.flush();
   }
   function close()
   {
      this.flush();
   }
   function flush(minDiskSpace)
   {
      if(minDiskSpace == undefined)
      {
         minDiskSpace = ank.utils.SharedObjectFix._soDefaultRequestedSize;
      }
      ank.utils.SharedObjectFix._so.data._Data[this._name] = this.data;
      return ank.utils.SharedObjectFix._so.flush(minDiskSpace);
   }
   function getSize()
   {
      return ank.utils.SharedObjectFix.getDiskUsage();
   }
   function connect(myConnection)
   {
      ank.utils.SharedObjectFix._so.data._Data[this._name] = this.data;
      return ank.utils.SharedObjectFix._so.connect(myConnection);
   }
   function send(handlerName)
   {
      ank.utils.SharedObjectFix._so.data._Data[this._name] = this.data;
      ank.utils.SharedObjectFix._so.send(handlerName);
   }
   function setFps(updatesPerSecond)
   {
      ank.utils.SharedObjectFix._so.data._Data[this._name] = this.data;
      return ank.utils.SharedObjectFix._so.setFps(updatesPerSecond);
   }
   static function getLocal(name)
   {
      if(!ank.utils.SharedObjectFix._oLocalCache[name])
      {
         ank.utils.SharedObjectFix._oLocalCache[name] = new ank.utils.SharedObjectFix(name);
      }
      return ank.utils.SharedObjectFix._oLocalCache[name];
   }
   static function deleteAll(url)
   {
      ank.utils.SharedObjectFix._oLocalCache = new Object();
      ank.utils.SharedObjectFix._so.clear();
   }
   static function getDiskUsage(url)
   {
      return SharedObject.getDiskUsage(url);
   }
}
