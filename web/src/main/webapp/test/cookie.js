function fixCookieDate (date)
{
var base = new Date(0);
var skew = base.getTime();
if (skew > 0)
        date.setTime (date.getTime() - skew);
}

function setCookie (name,value,expires,path,domain,secure)
{
document.cookie = name + "=" + escape (value) +
        ((expires) ? "; expires=" + expires.toGMTString() : "") +
        ((path) ? "; path=" + path : "; path=/") +
        ((domain) ? "; domain=" + domain : "") +
        ((secure) ? "; secure" : "");
}

function getCookieVal (offset)
{
var endstr = document.cookie.indexOf (";", offset);
if (endstr == -1)
        endstr = document.cookie.length;
return unescape(document.cookie.substring(offset, endstr));
}

function getCookie (name)
{
var arg = name + "=";
var alen = arg.length;
var clen = document.cookie.length;
var i = 0;
while (i < clen)
        {
        var j = i + alen;
    if (document.cookie.substring(i, j) == arg)
        return getCookieVal (j);
        i = document.cookie.indexOf(" ", i) + 1;
    if (i == 0)
                break;
        }
return null;
}

function deleteCookie (name,path,domain)
{
if (getCookie(name))
        {
        document.cookie = name + "=" +
                ((path) ? "; path=" + path : "") +
                ((domain) ? "; domain=" + domain : "") +
                "; expires=Thu, 01-Jan-70 00:00:01 GMT";
        }
}

function setAuthCode () {
	if (getCookie("authcode")) {
		if (document.forms['action'] && document.forms['action'].authcode) {
			document.forms['action'].authcode.value = getCookie("authcode");
		}
	}
}
