L.Control.PixelVal = L.Control.extend({
    active:false,
    options:{
        title: 'seek pixel value',
        position: 'bottomleft',
        hidden: false,
    },
    onAdd: function(){
        this.mapContainer = this._map.getContainer();
        var container = L.DomUtil.create('div', 'leaflet-control-pixelVal leaflet-bar leaflet-control');

        this._addCss();

        L.DomEvent.addListener(container, 'mouseup', this._toggleSeekButtons, this);

        var btnClass = 'leaflet-control-pixelVal-button'

        this.link = L.DomUtil.create('a', btnClass, container);
        this.link.id = "leafletPixelValue";
        this.link.title = this.options.title;

        // L.DomEvent.disableClickPropagation(container);
        return container;
    },
    show:function(){
        var controlContainer = document.getElementsByClassName("leaflet-control-pixelVal")[0];
        controlContainer.style.display = 'block';
    },
    hide:function(){
        var controlContainer = document.getElementsByClassName("leaflet-control-pixelVal")[0];
        controlContainer.style.display = 'none';
    },
    _toggleSeekButtons:function(e){
        // var content = $('#leafletPixelValue');
        if(this.active){
            rec.off({mousedown:showDataValue}); //rec定义在map部分
            this.active = false;
        }else{
            rec.on({mousedown:showDataValue}); //rec定义在map部分
            this.active = true;
        }
    },
    _addCss:function(){
        var css = document.createElement("style");
        css.type = "text/css";
        css.innerHTML = '.leaflet-control-pixelVal-button{' +
            'background-image: url(images/target.svg);'+
            'background-size: 16px 16px;'+
            'cursor: pointer;'+

        '}'
        document.body.appendChild(css);
    }
});