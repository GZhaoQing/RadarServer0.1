var DataList = {
    curImgBounds:undefined,
    curData:undefined,
};

var curImgBounds;
var curData;
//code for map (leaflet)
var tile_group=L.layerGroup();
var Common_accessToken= 'pk.eyJ1IjoidWZvcmljaGFyZCIsImEiOiJjamQwMDU2Y3kxajhkMnhvMXA0aXRtMzNkIn0.XAScbtETx_JRjPjFmo8Gkg';
var t_row=L.tileLayer('https://api.tiles.mapbox.com/styles/v1/{id}/tiles/256/{z}/{x}/{y}?access_token={accessToken}', {
    maxZoom: 18,
    id:'uforichard/cjdygtkk90y372sn5qk9hngk7',
    accessToken: Common_accessToken
}).addTo(tile_group);
var t_boundary=L.tileLayer('https://api.tiles.mapbox.com/styles/v1/{id}/tiles/256/{z}/{x}/{y}?access_token={accessToken}', {
    maxZoom: 18,
    id:'uforichard/cjdyfqezq8xek2sm1dnu0h65m',
    accessToken: Common_accessToken
}).addTo(tile_group);
var t_road=L.tileLayer('https://api.mapbox.com/styles/v1/{id}/tiles/256/{z}/{x}/{y}?&access_token={accessToken}', {
    maxZoom: 18,
    id:'uforichard/cjdy8tmjw0idw2rrwrhgsfohb',
    accessToken: Common_accessToken
}).addTo(tile_group);
var t_label=L.tileLayer('https://api.mapbox.com/styles/v1/{id}/tiles/256/{z}/{x}/{y}?&access_token={accessToken}', {
    maxZoom: 18,
    id:'uforichard/cjdygdk658y032sm1mytzw4yx',
    accessToken: Common_accessToken
}).addTo(tile_group);

//layers: [t_row,t_boundary, t_road,t_label],
var mymap = L.map('map',{
        center:[40.72,-74.2],
        zoom:7,
        layers: tile_group,
        fullscreenControl: true,
        fullscreenControlOptions: {
            position: 'topleft'
        }
    }
) //初始地图定义

//图层控制
var baseMaps={

}
var overlayMaps={
    "row":t_row,
    "boundary":t_boundary,
    "road": t_road,
    "label": t_label
}
L.control.layers(baseMaps, overlayMaps).addTo(mymap)

var ZoomViewer = L.Control.extend({
    onAdd: function(){
        var gauge = L.DomUtil.create('div');
        gauge.style.width = '100px';
        gauge.style.background = 'rgba(255,255,255,0.5)';
        gauge.style.textAlign = 'left';
        mymap.on('zoomstart zoom zoomend', function(ev){
            gauge.innerHTML = 'Zoom level: ' + mymap.getZoom();
        })
        return gauge;
    }
});
(new ZoomViewer({position:'bottomleft'})).addTo(mymap);

//        var singleOverlay=L.imageOverlay();
var group=L.layerGroup();
var rec;
var info = L.control();
var pixelValControl = new L.Control.PixelVal().addTo(mymap);
var popup = L.popup();
function showImg(imageUrl,hf){
    var imageBounds=hf.bounds;
    DataList.curImgBounds = imageBounds;
    //singleOverlay.setUrl(imageUrl).setBounds(imageBounds).addTo(mymap);
    var over=L.imageOverlay(imageUrl,imageBounds,{interactive:true});
    //hover特效
    rec=L.rectangle(imageBounds,{color: "#ff7800", weight: 0.2,opacity:0,fillOpacity:0})//.addTo(mymap);
    rec.on({
        mouseover:highlight,
        mouseout:reset_highlight
    });

    group.clearLayers().addLayer(rec).addLayer(over).addTo(mymap);
    mymap.fitBounds(imageBounds);

    info.update(hf);
    // var curData = DataList.curData;
    if(curData.type == 2||curData.type == 3){
        pixelValControl.show();
    }else{
        pixelValControl.hide();
    }

}
function highlight(e){
    e.target.setStyle({opacity:1});
}
function reset_highlight(e){
    e.target.setStyle({opacity:0});
}

function showDataValue(e){
    // try{
        var cur_lat = e.latlng.lat;
        var cur_lng = e.latlng.lng;
        var curImgBounds = DataList.curImgBounds;
        var lat_min = curImgBounds[0][0];
        var lat_max = curImgBounds[1][0];
        var lon_min = curImgBounds[0][1];
        var lon_max = curImgBounds[1][1];

        var data_x ;
        var data_y ;
        var value;
        // var curData = DataList.curData;
        if(curData.type == 2||curData.type == 3){
            if(lat_min > lat_max){
                var t1=lat_min;
                lat_min=lat_max;
                lat_max=t1;
            }if(lon_min > lon_max){
                var t2=lon_min;
                lon_min=lon_max;
                lon_max=t2;
            }
            data_x = Math.round(curData.m_nCols * (cur_lng - lon_min)/(lon_max-lon_min)) + curData.m_llx;
            data_y = Math.round(curData.m_nRows * (lat_max - cur_lat)/(lat_max-lat_min)) + curData.m_lly;
            value = curData.m_data[data_x][data_y];
            console.log(data_x+"||"+data_y + "::"+ value);
            popup.setLatLng([cur_lat,cur_lng])
                .setContent('<p>lon:'+cur_lng.toFixed(2)+',lat:'+cur_lat.toFixed(2)+'<br />x:'+data_x+',y:'+data_y+'<br />value:'+value+'</p>')
                .openOn(mymap);
        }else if(curData.type == 1){

        }
}
//经纬网
// Add a basic graticule with divisions every 20 degrees
// as a layer on a map
// L.graticule().addTo(mymap);

// Specify divisions every 10 degrees
// L.graticule({ interval: 10 }).addTo(map);

//Specify bold red lines instead of thin grey lines
L.graticule({
    style: {
        color: '#222',
        weight: 0.5
    }
}).addTo(mymap);

//print
var printer = L.easyPrint({
    tileLayer: [t_row,t_boundary, t_road,t_label],
    sizeModes: ['Current'],
    filename: 'myMap',
    exportOnly: true,
    hideControlContainer: true
}).addTo(mymap);
// L.control.browserPrint({
//     title: 'Just print me!',
//     printLayer: tile_group,
//     closePopupsOnPrint: false,
//     printModesNames: {Portrait:"Portrait", Landscape:"Paysage", Auto:"Auto", Custom:"Séléctionnez la zone"}
// }).addTo(mymap);
/************** draw *************/
drawnItems = L.featureGroup().addTo(mymap);
mymap.addControl(new L.Control.Draw({
    position: 'bottomleft',
    edit: {
        featureGroup: drawnItems,
        poly: {
            allowIntersection: false
        }
    },
    draw: {
        circlemarker: false,
        polygon: {
            allowIntersection: false,
            showArea: true
        }
    }
}));
mymap.on(L.Draw.Event.CREATED, function (event) {
    var layer = event.layer;
    drawnItems.addLayer(layer);
});

/*********************             文件控制            ********************/
//        var fileList=[];
var testfiles=["KFWD_SDUS64_NCZGRK_201208150217",
    "SATE_L2_F2G_VISSR_MWB_LBT_SEC_LCN-IR2-20170527-0100.AWX",
    "KEWX_SDUS54_N0VEWX_201707270600"]
/******** 入口 ********/
refrashFileList();

function refrashFileList(e){
    if(e === undefined || e === null){
        $.ajax({
            url:'/radar/rest/r/files/allList',
            type:"GET",
            // async:false,
            success:function(result){
                var json=JSON.parse(result);
                init(json.files);
                initInfo();
            }
        });
    }else{
        $.ajax({
            url:'/radar/rest/r/files/allList/'+e,
            type:"GET",
            // async:false,
            success:function(result){
                var json=JSON.parse(result);
                refrash(json.files);
                initInfo();
            }
        });
    }
    var refrash=function (file) {
        var k = 0;
        var params = file.param;
        var text = params.timeStart + "----" + params.timeEnd;
        $('div[id ^= "list_tab_"]').each(function(){
            //文件列表选项卡
            var $dom = $($(this)[k++]);
            var $tap = $dom.find('.tab_btn');
            if($tap.html()===file.doc){
                //填充文件名
                var containt = $dom.find('.tab_containt');
                for(var i=0;i<file.files.length;i++){
                    containt.append("<li><a class='file'>"+file.files[i]+"</a></li>");
                }
                containt.find('.file').each(function(){
                    setClickEach($(this));
                });
            }
        })
    }
}

function init(files){
    // var $ul = $('#list:first');
    for(var k = 0; k<files.length; k++){
        var $dom=$('#list_tab_'+k);
        $dom.find('.tab_btn').html(files[k].doc.toString()).bind('click',function(){
            $('.tab_containt').each(function(){
                $(this).removeClass("tab_show")
            })
            $(this).parent().find('.tab_containt').addClass("tab_show")
        })
        //填充文件名
        var containt = $dom.find('.tab_containt').empty();
        for(var i=0; i<files[k].files.length; i++){
            containt.append("<li><a class='file'>"+files[k].files[i]+"</a></li>")
        }
    }
    $('.file').each(function(){
        setClickEach($(this));
    });
    // var $pages=$('#page_wrap.pages');
    // for(var i=0;i<files.length/10;i++){
    //     $pages.append("<li><a>"+i+"</a></li>")
    // }

    //pixel value bar
    pixelValControl.hide();

    //var $ul = $('#list:first');//console.log(files);
    // var k=0;
    // for(var f in files){
    //     //文件列表选项卡
    //     var $dom=$('#list_tab_'+k);
    //     $dom.find('.tab_btn').html(f.toString()).bind('click',function(){
    //         $('.tab_containt').each(function(){
    //             $(this).removeClass("tab_show")
    //         })
    //         $(this).parent().find('.tab_containt').addClass("tab_show")
    //     })
    //     //填充文件名
    //     var containt=$dom.find('.tab_containt').empty();
    //     for(var i=0;i<files[f].length;i++){
    //         containt.append("<li><a class='file'>"+files[f][i]+"</a></li>")
    //     }
    //     k++
    // }
    //
    // $('.file').each(function(){
    //     setClickEach($(this));
    // });
    // var $pages=$('#page_wrap.pages');
    // for(var i=0;i<files.length/10;i++){
    //     $pages.append("<li><a>"+i+"</a></li>")
    // }
    //
    // //pixel value bar
    // pixelValControl.hide();
}

function initInfo(){ //信息框
    info.onAdd = function (map) {
        this._div = L.DomUtil.create('div', 'info'); // create a div with a class "info"
        this.update();
        return this._div;
    };
    info.update = function (props) {
        this._div.innerHTML = '<h4>File Info</h4>' +  (props ?
            '<b>' + props.time + '</b><br />'
            /*'<h5>透明度</h5><input id="opa_bar" style="range" max="100" min="0" value="100" oninput="change_transparency()">'*/
            : '');
    };

    info.addTo(mymap);
}
//设置列表项点击事件
function setClickEach(dom){
    dom.click(function(){
        getData($(this).html());
        $('.file').each(function(){ $(this).removeClass("list_active") });
        $(this).addClass("list_active");
    })
}

function getData(file){
    var json;
    $.ajax({
        url:'/radar/rest/r/files/'+file,
        type:"GET",
        // async:false,
        success:function(result){
            json=JSON.parse(result)
            curData = json.imgData
            curData.type = json.imgType;
            var hf=json.headfile;
            var bounds=hf.bounds;
            var img=json.imgUrl;
            showImg(img,hf);
            // console.log(DataList.curData)
        }
    });
}

/**********************播放*************************/
var key;
var index=0;
var listData;
var chooseList=["KEWX_SDUS54_N0VEWX_201707270600","KFWD_SDUS64_NCZGRK_201208150217"];
var play_flag=false;
Array.prototype.indexOf = function(val) {
    for (var i = 0; i < this.length; i++) {
        if (this[i] == val) return i;
    }
    return -1;
};
function playGallery(){
    resetOverlay();
    if(play_flag===false){
        //前端效果变动
        $('#left_play').css("background-image","Url('images/player-pause.svg')");
        play_flag=true;

        listData=new Array();
        for(var i=0;i<chooseList.length;i++){
            $.ajax({
                url: '/radar/rest/r/files/' + chooseList[i],
                type: "GET",
                async:false,
                success: function (result) {
                    console.log("success")
                    json = JSON.parse(result);
                    var hf = json.headfile;
                    // var bounds = hf.bounds;
                    var img = json.imgUrl;
                    listData[i]=new Object();
                    listData[i].hf=hf;
                    listData[i].img=img;
                    listData[i].imgType=json.imgType;
                }
            })
        }
//                console.log(chooseList.length+";;"+listData.length)
//         for(var i=0;i<chooseList.length;i++){
//             console.log(listData[i]);
//         }
        key=setInterval(function () {
            if(index<chooseList.length){
                console.log(listData[index].img);
                curData = listData[index].imgType;
                showImg(listData[index].img,listData[index].hf);
                index++;
            }else{
                pauseGallery();
            }
        },1000)
    }else if(play_flag===true){
        pauseGallery();

    }

}
function pauseGallery(){
    play_flag=false;
    $('#left_play').css("background-image","Url('images/player-go.svg')");
    key=clearInterval(key);
    $('.file').each(function(){
        $(this).unbind('click').removeClass("list_active");
        setClickEach($(this))
    });
    index=0;

}
/*********************播放前的手动挑选************************/
function chooseFile(dom){
    setOverlay()
    chooseList=new Array();
    $('.file').each(function(){
        setClickChoose($(this))
    })
}
function setClickChoose(dom){
    dom.unbind('click').bind('click',function(){
        // console.log($(this).html());
        chooseList.push($(this).html());
        $(this).addClass("list_active");
        for(var i=0;i<chooseList.length;i++){
            console.log(chooseList[i]);
        }
        setClickCancel(dom)
    })
}
function setClickCancel(dom){
    dom.unbind('click').click(function(){
        var i=chooseList.valueOf($(this).html());
        if(i!==-1){
            chooseList.splice(i,1);
        }
        $(this).removeClass("list_active");
        setClickChoose(dom);
    })
}
//高亮
function setOverlay(){
    $('#left_play').addClass('showAboveOverlay');
    $('#list').addClass('showAboveOverlay');
    $('#overlay_hide').attr('id','overlay_show');
}
function resetOverlay(){
    $('#overlay_show').attr('id','overlay_hide');
    $('#list').removeClass('showAboveOverlay');
    $('#left_play').removeClass('showAboveOverlay');
}

/************************搜索 **************************/
var searchResult;
var searchShowflag=false;
function taggleSearchForm(){
    if(searchShowflag){
        $('.toolbar').animate({height:"60px"});
        $('#search').removeClass("search_show").addClass("search_hide");
        searchShowflag=false
    }else{
        $('.toolbar').animate({height:"160px"});
        $('#search').removeClass("search_hide").addClass("search_show");
        searchShowflag=true;
    }
}
function do_search(){
    var dataType = $('.listActive').find('.tab_btn').html();
    var ts=new Date($('#time_start').val()).toISOString();
    var te=new Date($('#time_end').val()).toISOString();
    var params={
        dataType:dataType,
        timeStart:ts,
        timeEnd:te};
    var refrash=function (file) {
        var k = 0;
        var params = file.param;
        var text = params.timeStart + "----" + params.timeEnd;
        $('div[id ^= "list_tab_"]').each(function(){
            //文件列表选项卡
            var $dom = $($(this)[k++]);
            var $tap = $dom.find('.tab_btn');
            if($tap.html()===file.doc){
                //填充文件名
                var containt = $dom.find('.tab_containt');
                if(containt.find(".search_result_tab").length === 0){
                    containt.empty();
                }
                containt.append('<li class="search_result_tab"><h3  onclick="taggleSearchResultList(this)"><a onclick="cancelSingleSearch(this)" >X</a>'+ text + '<i class="arrowRot"></i></h3></li>');
                var ul = document.createElement("ul");
                var $ul = $(ul).addClass("search_result_containt");
                for(var i=0;i<file.files.length;i++){
                    $ul.append("<li><a class='file'>"+file.files[i]+"</a></li>");
                }
                containt.append($ul);
                containt.find('.file').each(function(){
                    setClickEach($(this));
                });
            }
        })
    }
    $.ajax({
        type: "POST",
        url: "/radar/rest/r/files/search",
        data: JSON.stringify(params),
        contentType:"application/json",
        success: function(result){
            var json = JSON.parse(result).AllFiles;
            // console.log(json);
            if(json!==undefined && json!==null){
                json.param = params;
                refrash(json);
            }
            $('#crumb_list').removeClass('search_hide')//.find('span').html($('#time_start').val()+"TO"+$('#time_end').val());
        }
    });
}
function cancelSearch(){
    $('#crumb_list').addClass('search_hide');
    refrashFileList();
}

function change_transparency(){
    var value = $('#opa_bar').val();
    group.setStyle({opacity:value});
}
