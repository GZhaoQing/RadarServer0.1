
//code for map (leaflet)
var Common_accessToken= 'pk.eyJ1IjoidWZvcmljaGFyZCIsImEiOiJjamQwMDU2Y3kxajhkMnhvMXA0aXRtMzNkIn0.XAScbtETx_JRjPjFmo8Gkg';
var t_row=L.tileLayer('https://api.tiles.mapbox.com/styles/v1/{id}/tiles/256/{z}/{x}/{y}?access_token={accessToken}', {
    maxZoom: 18,
    id:'uforichard/cjdygtkk90y372sn5qk9hngk7',
    accessToken: Common_accessToken
});
var t_boundary=L.tileLayer('https://api.tiles.mapbox.com/styles/v1/{id}/tiles/256/{z}/{x}/{y}?access_token={accessToken}', {
    maxZoom: 18,
    id:'uforichard/cjdyfqezq8xek2sm1dnu0h65m',
    accessToken: Common_accessToken
});
var t_road=L.tileLayer('https://api.mapbox.com/styles/v1/{id}/tiles/256/{z}/{x}/{y}?&access_token={accessToken}', {
    maxZoom: 18,
    id:'uforichard/cjdy8tmjw0idw2rrwrhgsfohb',
    accessToken: Common_accessToken
});
var t_label=L.tileLayer('https://api.mapbox.com/styles/v1/{id}/tiles/256/{z}/{x}/{y}?&access_token={accessToken}', {
    maxZoom: 18,
    id:'uforichard/cjdygdk658y032sm1mytzw4yx',
    accessToken: Common_accessToken
});

var mymap = L.map('map',{center:[40.72,-74.2],zoom:7,layers: [t_row,t_boundary, t_road,t_label]})
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
(new ZoomViewer({position:'bottomleft'})).addTo(mymap)

//        var singleOverlay=L.imageOverlay();
var group=L.layerGroup();
var rec
function showImg(imageUrl,hf){
    var imageBounds=hf.bounds;
    //singleOverlay.setUrl(imageUrl).setBounds(imageBounds).addTo(mymap);
    var over=L.imageOverlay(imageUrl,imageBounds,{interactive:true});

    //hover特效
    rec=L.rectangle(imageBounds,{color: "#ff7800", weight: 1,opacity:0,fillOpacity:0})//.addTo(mymap);
    rec.on({
        mouseover:highlight,
        mouseout:reset_highlight
    })

    group.clearLayers().addLayer(over).addLayer(rec).addTo(mymap);
    mymap.fitBounds(imageBounds);

    info.update(hf);
}
function highlight(e){
    e.target.setStyle({opacity:1});
}
function reset_highlight(e){
    e.target.setStyle({opacity:0});
}

//        var fileList=[];
var testfiles=["KFWD_SDUS64_NCZGRK_201208150217",
    "SATE_L2_F2G_VISSR_MWB_LBT_SEC_LCN-IR2-20170527-0100.AWX",
    "KEWX_SDUS54_N0VEWX_201707270600"]
getFileList();

function init(files){
    //var $ul = $('#list:first');//console.log(files);
    var k=0;
    for(var f in files){
        //文件列表选项卡
        var $dom=$('#list_tab_'+k);
        $dom.find('.tab_btn').html(f.toString()).bind('click',function(){
            $('.tab_containt').each(function(){
                $(this).removeClass("tab_show")
            })
            $(this).parent().find('.tab_containt').addClass("tab_show")
        })
        //填充文件名
        for(var i=0;i<files[f].length;i++){
            $dom.find('.tab_containt').append("<li><a class='file'>"+files[f][i]+"</a></li>")
        }
        k++
    }

    $('.file').each(function(){
        setClickEach($(this));
    });
    var $pages=$('#page_wrap.pages');
    for(var i=0;i<files.length/10;i++){
        $pages.append("<li><a>"+i+"</a></li>")
    }
}

var info = L.control();
function initInfo(){
    info.onAdd = function (map) {
        this._div = L.DomUtil.create('div', 'info'); // create a div with a class "info"
        this.update();
        return this._div;
    };
    info.update = function (props) {
        this._div.innerHTML = '<h4>File Info</h4>' +  (props ?
            '<b>' + props.time + '</b><br />'
            : '');
    };

    info.addTo(mymap);
}
//列表项
function setClickEach(dom){
    dom.click(function(){
        getData($(this).html());
        $('.file').each(function(){$(this).removeClass("list_active")});
        $(this).addClass("list_active");
    })
}

function getFileList(){
    $.ajax({
        url:'/radar/rest/r/files/list',
        type:"GET",
        // async:false,
        success:function(result){
            var json=JSON.parse(result);
//                        fileList=json;
            init(json);
            initInfo();
        }
    });
}
function getData(file){
    var json;
    $.ajax({
        url:'/radar/rest/r/files/'+file,
        type:"GET",
        // async:false,
        success:function(result){
            json=JSON.parse(result)
            var hf=json.headfile;
            var bounds=hf.bounds;
            var img=json.imgUrl;
            showImg(img,hf);
            // switch(type){
            // case 1:
            // c=createGridImage_Satellite(array,array[0].length,array.length);
            // break;
            // case 2:
            // c=createGridImage_Radar(array,array[0].length,array.length);
            // break;
            // case 3:
            // c=createRadialImage(array,data.azimuth,data.gNum);
            // break;
            // }
            // console.log("getData:"+c.width);
            // setStyle(c);
        }
    });
}

//gallery
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

    resetOverlay()
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
                    json = JSON.parse(result)
                    var hf = json.headfile;
                    var bounds = hf.bounds;
                    var img = json.imgUrl;
                    listData[i]=new Object();
                    listData[i].hf=hf;
                    listData[i].img=img;
                }
            })
        }
//                console.log(chooseList.length+";;"+listData.length)
        for(var i=0;i<chooseList.length;i++){
            console.log(listData[i]);
        }
        key=setInterval(function () {
            if(index<chooseList.length){
                console.log(listData[index].img)
                showImg(listData[index].img,listData[index].hf);
                index++;
            }else{
                pauseGallery()
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
//手动挑选
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

//搜索
var searchShowflag=false;
function taggleSearchForm(){
    if(searchShowflag){
        $('.toolbar').animate({height:"60px"})
        $('#search_form').removeClass("search_show").addClass("search_hide")
        searchShowflag=false
    }else{
        $('.toolbar').animate({height:"160px"})
        $('#search_form').removeClass("search_hide").addClass("search_show")
        searchShowflag=true
    }
}
function do_search(){
    var params={"timeStart":$('#time_start').val(),
        "timeEnd":$('#time_end').val()};

    $.ajax({
        type: "POST",
        url: "/radar/rest/r/files/",
        data: params,
        dataType : "json",
        success: function(result){

        }
    });
}
