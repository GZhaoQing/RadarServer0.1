function cancelSingleSearch(dom_this){
    if(event.target===dom_this){
        event.stopPropagation();
        $('#crumb_list').addClass('search_hide');
        var containt = $(dom_this).parents(".tab_containt");
        var search_containt = $(dom_this).parents(".search_result_tab");
        search_containt.next().remove();
        search_containt.remove();
        var lasted = containt.find(".search_result_tab");
        if(lasted === undefined || lasted === null || lasted.length === 0){
            var doc = containt.siblings("tab_btn").html();
            refrashFileList(doc);
        }
    }
}
function taggleSearchResultList(dom_this){
     // console.log(1);
    // var containt = $(dom_this).parents(".tab_containt");
    var search_containt = $(dom_this).parents(".search_result_tab");
    var search_resule_containt = search_containt.next();
    if(search_resule_containt.hasClass("search_result_hide")){
        $(dom_this).find('i').addClass("arrowRot");
        search_resule_containt.removeClass("search_result_hide");
    }else{
        $(dom_this).find('i').removeClass("arrowRot");
        search_resule_containt.addClass("search_result_hide");
    }

}


function adaptListHeight(){
    var winHeight = window.innerHeight;
    var mapHeight = $("#map").height();
    var cantaint = $('.tab_containt')
    if(cantaint.height() > (winHeight-75)){
        $('.tab_containt').height(winHeight-75);
    }
}