
function AddCriteriaWindow(ruleid, id) {
	url="/recommend/criteria_detail.jsp?ruleid="+ruleid+"&id="+id;
	windowname="CRITERIA";
        popup=window.open(url,windowname,"height=610,width=550,status=no,toolbar=no,directories=no,menubar=no,scrollbars=yes,location=no");
}

function DeleteCriteria(ruleid,id) {
        if (confirm("Are you sure you want to delete this Criteria?")) {
            location.href="/recommend/criteria_detail.jsp?ruleid="+ruleid+"&delid="+id;

        }
}

function refreshRule(ruleid) {
        location.href="/recommend/rule_detail.jsp?id="+ruleid;
}


function updateParent() {
      window.opener.location.reload();
      window.close();
}

// 
// I'm guessing what follows is not used 
// 

function addLink(fromtype,totype,fromid) {
	url="/admin/add_link.php?fromtype="+fromtype+"&totype="+totype+"&fromid="+fromid;
	windowname="PHOTO";
        popup=window.open(url,windowname,"height=396,width=589,status=no,toolbar=no,directories=no,menubar=no,scrollbars=yes,location=no");
}

function addInstructor(eventid,instructorid) {
	url="/admin/add_instructor.php?ev="+eventid+"&inst="+instructorid;
	windowname="PHOTO";
        popup=window.open(url,windowname,"height=396,width=589,status=no,toolbar=no,directories=no,menubar=no,scrollbars=yes,location=no");
}

function addCategory(productid) {
	url="/admin/add_category.php?prod="+productid;
	windowname="PHOTO";
        popup=window.open(url,windowname,"height=396,width=589,status=no,toolbar=no,directories=no,menubar=no,scrollbars=yes,location=no");
}

function addfilter(productid) {
	url="/php/admin/add_filter.php?prodid="+productid;
	windowname="PHOTO";
        popup=window.open(url,windowname,"height=396,width=589,status=no,toolbar=no,directories=no,menubar=no,scrollbars=yes,location=no");
}

function openEditProductFeatureElementWindow(elementid) {
	url="/php/admin/productfeatureelementedit.php?elementid="+elementid;
	windowname="PHOTO";
        popup=window.open(url,windowname,"height=475,width=589,status=no,toolbar=no,directories=no,menubar=no,scrollbars=yes,location=no");
}

function openViewAssetWindowCustom(assetid) {
	url="/php/asset.php?id="+assetid;
	windowname="ASSET";
        popup=window.open(url,windowname,"height=475,width=589,status=no,toolbar=no,directories=no,menubar=no,scrollbars=yes,location=no");
}

function openAddAssetWindowCustom(asset,type,assetid) {
	url="/php/admin/addasset.php?asset="+asset+"&type="+type+"&assetid="+assetid;
	windowname="ASSET";
        popup=window.open(url,windowname,"height=475,width=589,status=no,toolbar=no,directories=no,menubar=no,scrollbars=yes,location=no");
}

function openExistingAssetWindowCustom(asset,type,assetid) {
	url="/php/admin/existingasset.php?asset="+asset+"&type="+type+"&assetid="+assetid;
	windowname="ASSET";
        popup=window.open(url,windowname,"height=475,width=700,status=no,toolbar=no,directories=no,menubar=no,scrollbars=yes,location=no");
}

function AddProductFeatureElementWindow(prodid,featureid) {
	url="/php/admin/productfeatureelementedit.php?prodid="+prodid+"&featureid="+featureid;
	windowname="PHOTO";
        popup=window.open(url,windowname,"height=475,width=589,status=no,toolbar=no,directories=no,menubar=no,scrollbars=yes,location=no");
}

function AddTeaserPlacementWindow(teaserid,type,id) {
	url="/php/admin/teaserplacement.php?teaserid="+teaserid+"&type="+type+"&id="+id;
	windowname="PHOTO";
        popup=window.open(url,windowname,"height=610,width=550,status=no,toolbar=no,directories=no,menubar=no,scrollbars=yes,location=no");
}

function AddProductOptionWindow(prodid,optionid) {
	url="/php/admin/add_options.php?prodid="+prodid+"&optionid="+optionid;
	windowname="PHOTO";
        popup=window.open(url,windowname,"height=475,width=589,status=no,toolbar=no,directories=no,menubar=no,scrollbars=yes,location=no");
}

function AddFacetLabelWindow(prodid) {
	url="/php/admin/add_facet_label.php?prodid="+prodid;
	windowname="PHOTO";
        popup=window.open(url,windowname,"height=475,width=589,status=no,toolbar=no,directories=no,menubar=no,scrollbars=yes,location=no");
}

function AddProductLabelWindow(labelid) {
	url="/php/admin/add_product_label.php?labelid="+labelid;
	windowname="PHOTO";
        popup=window.open(url,windowname,"height=475,width=589,status=no,toolbar=no,directories=no,menubar=no,scrollbars=yes,location=no");
}

function AddProductCatWindow(catid) {
	url="/php/admin/add_product_cat.php?catid="+catid;
	windowname="PHOTO";
        popup=window.open(url,windowname,"height=475,width=589,status=no,toolbar=no,directories=no,menubar=no,scrollbars=yes,location=no");
}

function addfeature(productid) {
	url="/php/admin/add_features.php?prodid="+productid;
	windowname="PHOTO";
        popup=window.open(url,windowname,"height=396,width=589,status=no,toolbar=no,directories=no,menubar=no,scrollbars=yes,location=no");
}

function openAddPDFWindow(table,field) {
	url="/admin/add_pdf.php?table="+table+"&field="+field;
	windowname="PHOTO";
        popup=window.open(url,windowname,"height=396,width=589,status=no,toolbar=no,directories=no,menubar=no,scrollbars=yes,location=no");
}

function openAddPhotoWindow(table,field) {
	url="/admin/add_photo.php?table="+table+"&field="+field;
	windowname="PHOTO";
        popup=window.open(url,windowname,"height=396,width=589,status=no,toolbar=no,directories=no,menubar=no,scrollbars=yes,location=no");
}

function openChoosePhotoWindow(table,field) {
	url="/admin/choose_photo.php?table="+table+"&field="+field;
	windowname="PHOTO";
        popup=window.open(url,windowname,"height=396,width=589,status=no,toolbar=no,directories=no,menubar=no,scrollbars=yes,location=no");
}

function openHelpWindow(table,field) {
	url="/php/admin/help.php?table="+table+"&field="+field;
	windowname="HELP";
        popup=window.open(url,windowname,"height=396,width=589,status=no,toolbar=no,directories=no,menubar=no,scrollbars=yes,location=no");
}

function openEditorWindow(table,field,id) {
	url="/admin/editor.php?table="+table+"&field="+field+"&id="+id;
	windowname="EDITOR";
        popup=window.open(url,windowname,"height=525,width=589,status=no,toolbar=no,directories=no,menubar=no,scrollbars=yes,location=no,resizable=yes");
}

function clearTextField(formname,fieldname) {
	document.forms[formname].elements[fieldname].value = '';
}


function deleteProductFeatureElement(productfeatureelementid,id) {
        if (confirm("Are you sure you want to delete this Product Feature Element?")) {
            location.href="product_detail.php?deleleid="+productfeatureelementid+"&id="+id;
        }
}

function deleteTeaserPlacement(placementid,teaserid) {
        if (confirm("Are you sure you want to delete this Teaser Placement?")) {
            location.href="teaser_detail.php?delteaser="+placementid+"&id="+teaserid;
        }
}

function deleteProductOption(productoptionid,id) {
        if (confirm("Are you sure you want to delete this Product Option?")) {
            location.href="product_detail.php?remopt="+productoptionid+"&id="+id;
        }
}

function deleteFacetLabel(labelid,id) {
        if (confirm("Are you sure you want to delete this Facet Label?")) {
            location.href="product_detail.php?remlabel="+labelid+"&id="+id;
        }
}

function deleteProductLabel(labelid,id) {
        if (confirm("Are you sure you want to delete this Facet Label?")) {
            location.href="facet_label_detail.php?remlabel="+labelid+"&id="+id;
        }
}

function deleteProductCat(catid,id) {
        if (confirm("Are you sure you want to delete this Product Category?")) {
            location.href="dealer_category_detail.php?remcat="+catid+"&id="+id;
        }
}


function deleteProductFilter(productfilterid,id) {
        if (confirm("Are you sure you want to delete this Product Filter?")) {
            location.href="product_detail.php?remfilt="+productfilterid+"&id="+id;
        }
}

function clearPhoto(prefix) {
	urlfield = prefix+'imageurl';
	heightfield = prefix+'imageheight';
	widthfield = prefix+'imagewidth';
	formname = 'adminform';

	document.forms[formname].elements[urlfield].value = '';
	document.forms[formname].elements[heightfield].value = '0';
	document.forms[formname].elements[widthfield].value = '0';
}

// -->
