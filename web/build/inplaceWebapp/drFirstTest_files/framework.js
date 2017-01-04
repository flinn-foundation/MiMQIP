// javascript-common \ html \ javascript \ framework.js
/**
 * @fileOverview Script containing the DrFirst JavaScript framework.
 *
 * @author mkotelba
 */

/*===========================================================
 = CONSOLE
 ===========================================================*/

/**#nocode+*/

window.console = window.console ? window.console : {};

window.console.LEVELS = [ "trace", "debug", "info", "warn", "error", "fatal" ];

window.console.debug = window.console.debug ? window.console.debug : (window.console.log ? window.console.log : function () {});
window.console.trace = window.console.trace ? window.console.trace : window.console.debug;
window.console.info = window.console.info ? window.console.info : function () {};
window.console.warn = window.console.warn ? window.console.warn : function () {};
window.console.error = window.console.error ? window.console.error : function () {};
window.console.fatal = window.console.fatal ? window.console.fatal : window.console.error;

/**#nocode-*/

/*===========================================================
 = BUILT-IN EXTENSION: LOW-LEVEL
 ===========================================================*/

/**
 * Determines whether a given object is not undefined or null.
 *
 * @static
 * @param {Object} obj object to use
 * @return {Boolean} whether the given object is undefined or null
 */
Object.exists = function (obj)
{
	return !Object.isUndefined(obj) && !Object.isNull(obj);
}

/**
 * Gets the display name for a given object.
 *
 * @static
 * @param {Object} obj object to use
 * @param {String} defaultName optional default name to use
 * @return {String} default name of the object
 */
Object.getDisplayName = function (obj, defaultName)
{
	defaultName = Object.is(defaultName, String) ? defaultName : "";

	return Object.is(obj.__name, String) ? obj.__name : (Object.is(obj.name, String) ? obj.name : defaultName);
}

/**
 * Determines whether an object is an instance of a given type.
 *
 * @static
 * @param {Object} obj object to test
 * @param {Function} type class to test against
 * @return {Boolean} whether the object is an instance of the given type
 */
Object.is = function (obj, type)
{
	if (!(type instanceof Function) && (type != Element) && (!window.Window || (type != Window)) && (!window.Document || (type != window.Document)) &&
		(!window.Location || (type != window.Location)))
	{
		throw new TypeError("Type to check against must be a function: " + type);
	}

	return Object.exists(obj) && ((type == Object) || (type == Boolean) || (type == Number) || (type == String)) ?
		(obj.constructor == type) : (obj instanceof type);
}

/**
 * Determines whether a given object is undefined.
 *
 * @static
 * @param {Object} obj object to use
 * @return {Boolean} whether the given object is undefined
 */
Object.isUndefined = function (obj)
{
	return obj === undefined;
}

/**
 * Determines whether a given object is null.
 *
 * @static
 * @param {Object} obj object to use
 * @return {Boolean} whether the given object is null
 */
Object.isNull = function (obj)
{
	return obj === null;
}

/**
 * Extends a given object with the properties of another.
 *
 * @static
 * @param {Object} obj object to extend
 * @param {Object} data data to extend with
 * @return {Object} extended object
 */
Object.extend = function (obj, data)
{
	if (!Object.exists(obj))
	{
		throw new Error("An object to extend must exist.");
	}

	if (!Object.exists(data))
	{
		throw new Error("Extension data must exist.");
	}

	for (var property in data)
	{
		console.debug("Extending " + Object.getDisplayName(obj, "[object]") + ": " + property + "=" + data[property]);

		obj[property] = data[property];
	}

	return obj;
}

/*===========================================================
 = CLASS
 ===========================================================*/

/**
 * @class Abstract utility class specific to classes themselves.
 */
var Class =
/** @lends Class */
{
	/**
	 * Name of the class.
	 *
	 * @constant
	 * @static
	 * @type String
	 */
	__name: "Class",

	/**
	 * Creates a new class with the given name and static data.
	 *
	 * @static
	 * @param {String} className class name to use
	 * @return {Function} new class function
	 */
	create: function (className)
	{
		/**#nocode+*/

		className = Object.is(className, String) ? className : "";

		var clazz = function ()
		{
			if (Object.exists(this.__superConstruct))
			{
				var method = this.__superConstruct, context = this;

				this.__superConstruct = function ()
				{
					method.apply(context, arguments);
				}
			}
			else
			{
				this.__superConstruct = function () {};
			}

			this.__construct.apply(this, arguments);
		}

		clazz.__name = className;

		return Class.extend(clazz);

		/**#nocode-*/
	},

	/**
	 * Extends a class with a given super class.
	 *
	 * @static
	 * @param {Function} clazz class to extend
	 * @param {Function} superClass super class to extend with
	 * @return {Function} the extended class
	 */
	extend: function (clazz, superClass)
	{
		/**#nocode+*/

		if (Object.exists(superClass))
		{
			if (!Object.is(superClass, Function))
			{
				throw new TypeError("Super class must be a function.");
			}

			console.debug("Extending class: " + Class.getClassName(clazz, "[class]") + " <= " + Class.getClassName(superClass, "[class]"));

			Object.extend(clazz.prototype, superClass.prototype);

			clazz.prototype.__super = superClass;
			clazz.prototype.__superConstruct = superClass.prototype.__construct;

			clazz.__parentClasses = clazz.__parentClasses ? clazz.__parentClasses : [];
			clazz.__parentClasses.push(superClass);
		}

		if (!Object.is(clazz.prototype.__construct, Function))
		{
			console.debug("Creating default construction method for class: " + Class.getClassName(clazz, "[class]"));

			clazz.prototype.__construct = function () {};
		}

		return clazz;

		/**#nocode-*/
	},

	/**
	 * Gets the class name for a given class.
	 *
	 * @static
	 * @param {Function} clazz class function to use
	 * @param {String} defaultName optional default name to use
	 * @return {String} default name of the class
	 */
	getClassName: function (clazz, defaultName)
	{
		return Object.getDisplayName(clazz, defaultName);
	}
}

/*===========================================================
 = BUILT-IN EXTENSION: OBJECT
 ===========================================================*/

Object.extend(Object,
/** @lends Object */
{
	/**
	 * Default attribute prefix for building objects from XML.
	 *
	 * @constant
	 * @static
	 * @type String
	 */
	DEFAULT_ATTRIBUTE_PREFIX: "__",

	/**
	 * Builds an object from a given XML node.
	 *
	 * @static
	 * @param {Element} node XML node to build from
	 * @param {Boolean} includeAttributes whether to include attributes
	 * @param {String} attributePrefix prefix for attribute properties
	 * @return {Object} object representation of the XML node
	 */
	fromXML: function (node, includeAttributes, attributePrefix)
	{
		if (!Object.exists(node))
		{
			throw new Error("XML node must not be undefined or null.");
		}

		includeAttributes = Object.is(includeAttributes, Boolean) ? includeAttributes : true;
		attributePrefix = Object.is(attributePrefix, String) ? attributePrefix : Object.DEFAULT_ATTRIBUTE_PREFIX;

		var obj = {}, attributes = Object.exists(node.attributes) ? Array.asArray(node.attributes) : [],
			children = Object.exists(node.childNodes) ? Array.asArray(node.childNodes) : [];

		if ((children.length == 1) && (children.getFirst().nodeType == 3))
		{
			var value = children.getFirst().nodeValue;

			if (value.isBoolean())
			{
				value = Boolean.parse(value);
			}
			else if (value.isNumber())
			{
				value = Number.parse(value);
			}

			return value;
		}

		for (var attrIndex = 0; attrIndex < attributes.length; attrIndex++)
		{
			var attribute = attributes[attrIndex];

			obj[attributePrefix + attribute.nodeName] = attribute.nodeValue;
		}

		for (var childIndex = 0; childIndex < children.length; childIndex++)
		{
			var child = children[childIndex];

			if (child.nodeType == 1)
			{
				obj[child.nodeName] = Object.exists(obj[child.nodeName]) ? [ obj[child.nodeName], Object.fromXML(child) ] : Object.fromXML(child);
			}
		}

		return obj;
	},

	/**
	 * Builds an object from a given XML document.
	 *
	 * @static
	 * @param {Document} xml XML document to build from
	 * @return {Object} object representation of the XML node
	 */
	fromXMLDocument: function (xml)
	{
		if (!Object.exists(xml))
		{
			throw new Error("XML document must not be undefined or null.");
		}

		return (xml.childNodes.length > 0) ? Object.fromXML(xml.childNodes[0]) : {};
	},

	/**
	 * Attaches an event handler for a specific event to a given object.
	 *
	 * @static
	 * @param {Object} obj object to use
	 * @param {String} event name of the event to handle for
	 * @param {Function} handler event handler function attach
	 */
	handleEvent: function (obj, event, handler)
	{
		if (!Object.exists(obj))
		{
			throw new Error("Object must not be undefined or null.");
		}

		if (!Object.exists(event))
		{
			throw new Error("Event name must not be undefined or null.");
		}

		event = event.toString().toLowerCase();

		if (event.isEmpty())
		{
			throw new Error("Event name must not be empty.");
		}

		if (!Object.exists(handler))
		{
			throw new Error("Event handler must not be undefined or null.");
		}

		if (!Object.is(handler, Function))
		{
			throw new Error("Event handler must be a function.");
		}

		if (obj.addEventListener)
		{
			obj.addEventListener(event, handler, false);
		}
		else
		{
			event = (!event.startsWith("on") ? "on" : "") + event;

			if (obj.attachEvent)
			{
				obj.attachEvent(event, handler);
			}
			else if (obj[event])
			{
				var previousHandler = obj[event];

				obj[event] = function (e)
				{
					previousHandler(e);
					handler(e);
				}
			}
			else
			{
				obj[event] = handler;
			}
		}
	}
});

/*===========================================================
 = BUILT-IN EXTENSION: BOOLEAN
 ===========================================================*/

Object.extend(Boolean,
/** @lends Boolean */
{
	/**
	 * Pattern for strings that can resolve to a boolean value.
	 *
	 * @constant
	 * @static
	 * @type RegExp
	 */
	BOOLEAN_PATTERN: /true|false|yes|y|no|n/i,

	/**
	 * Pattern for strings that can resolve to true.
	 *
	 * @constant
	 * @static
	 * @type RegExp
	 */
	TRUE_PATTERN: /true|yes|y/i,

	/**
	 * Pattern for strings that can resolve to false.
	 *
	 * @constant
	 * @static
	 * @type RegExp
	 */
	FALSE_PATTERN: /false|no|n/i,

	/**
	 * Parses a given object into a boolean value.
	 *
	 * @static
	 * @param {Object} obj object to parse
	 * @return {Boolean} object as a boolean value
	 */
	parse: function (obj)
	{
		if (Object.is(obj, Boolean))
		{
			return obj;
		}
		else if (Object.is(obj, String) && obj.isBoolean(obj))
		{
			return obj.contains(Boolean.TRUE_PATTERN);
		}
		else
		{
			return false;
		}
	}
});

Object.extend(Boolean.prototype,
/** @lends Boolean.prototype */
{
	/**
	 * Gets the numerical representation of this boolean.
	 *
	 * @return numerical representation of this boolean
	 */
	toNumber: function ()
	{
		return this ? 1 : 0;
	}
});

/*===========================================================
 = BUILT-IN EXTENSION: NUMBER
 ===========================================================*/

Object.extend(Number,
/** @lends Number */
{
	/**
	 * Pattern for strings that can resolve to a numerical value.
	 *
	 * @constant
	 * @static
	 * @type RegExp
	 */
	NUMBER_PATTERN: /\d*\.?\d*/,

	/**
	 * Parses a given object into a numerical value.
	 *
	 * @static
	 * @param {Object} obj object to parse
	 * @return {Number} object as a numerical value
	 */
	parse: function (obj)
	{
		if (Object.is(obj, Number))
		{
			return obj;
		}
		else if (Object.is(obj, String) && obj.isNumber())
		{
			return obj * 1;
		}
		else
		{
			return 0;
		}
	}
});

/*===========================================================
 = BUILT-IN EXTENSION: STRING
 ===========================================================*/

Object.extend(String, 
/** @lends String */
{
	/**
	 * An empty string.
	 *
	 * @constant
	 * @static
	 * @type String
	 */
	EMPTY: ""
});

Object.extend(String.prototype, 
/** @lends String.prototype */
{
	/**
	 * Determines whether this string contains a given object.
	 *
	 * @param {Object} obj object to test with
	 * @param {Number} fromIndex index to begin the search at
	 * @return whether this string contains the given string
	 */
	contains: function (obj, fromIndex)
	{
		if (!Object.is(obj, String) && !Object.is(obj, RegExp) && !Object.is(obj, Array))
		{
			throw new TypeError("Object to test with must be a string, regular expression, or array of the first two.");
		}

		obj = !Object.is(obj, Array) ? [ obj ] : obj;

		for (var a = 0; a < obj.length; a++)
		{
			if (Object.is(obj[a], String))
			{
				if (this.indexOf(obj[a], fromIndex) == -1)
				{
					return false;
				}
			}
			else if (Object.is(obj[a], RegExp))
			{
				return obj[a].test(this);
			}
			else
			{
				throw new TypeError("Object to search with must be a string or regular expression.");
			}
		}

		return true;
	},

	/**
	 * Determines whether this string ends with a given string.
	 *
	 * @param {String} str string to test with
	 * @return {Boolean} whether this string ends with the given string
	 */
	endsWith: function (str)
	{
		if (!Object.is(str, String))
		{
			throw new TypeError("Target must be a string.");
		}

		var lengthDifference = this.length - str.length;

		return (lengthDifference >= 0) && (this.lastIndexOf(str) == lengthDifference);
	},

	/**
	 * Determines whether this string can be converted to a boolean value.
	 *
	 * @return {Boolean} whether this string can be converted to a boolean value
	 */
	isBoolean: function ()
	{
		return this.contains(Boolean.BOOLEAN_PATTERN);
	},

	/**
	 * Determines whether this string is empty.
	 *
	 * @return {Boolean} whether this string is empty
	 */
	isEmpty: function ()
	{
		return this.length == 0;
	},

	/**
	 * Determines whether this string can be converted to a numerical value.
	 *
	 * @return {Boolean} whether this string can be converted to a numerical value
	 */
	isNumber: function ()
	{
		return this.contains(Number.NUMBER_PATTERN);
	},

	/**
	 * Determines whether this string is a valid JavaScript variable name.
	 *
	 * @return {Boolean} whether this string is a valid JavaScript variable name
	 */
	isValidVariable: function ()
	{
		return this.contains(/^[\$a-zA-Z][\-\$\w]*$/);
	},

	/**
	 * Determines whether this string contains only alphanumeric characters and/or underscores.
	 *
	 * @return {Boolean} whether this string contains only alphanumeric characters and/or underscores
	 */
	onlyAlphaNumerics: function ()
	{
		return this.removeAll(/[^\w]/g);
	},

	/**
	 * Determines whether this string contains only letters.
	 *
	 * @return {Boolean} whether this string contains only letters
	 */
	onlyLetters: function ()
	{
		return this.removeAll(/[^a-z]/gi);
	},

	/**
	 * Determines whether this string contains only numbers.
	 *
	 * @return {Boolean} whether this string contains only numbers
	 */
	onlyNumbers: function ()
	{
		return this.removeAll(/[^\d]/g);
	},

	/**
	 * Removes a single occurance of a given object within this string.
	 *
	 * @return {String} this string with a single occurance of the object removed
	 */
	remove: function (obj)
	{
		return this.replace(obj, "");
	},

	/**
	 * Removes all occurances of a given object within this string.
	 *
	 * @return {String} this string with all occurances of the object removed
	 */
	removeAll: function (obj)
	{
		return this.replaceAll(obj, "");
	},

	/**
	 * Replaces all occurances of a string with a given replacement.
	 *
	 * @param {String|RegExp} obj object to replace
	 * @param {String} replacement replacement to use
	 * @return {String} string with all occurances replaced
	 */
	replaceAll: function (obj, replacement)
	{
		if (!Object.is(obj, String) && !Object.is(obj, RegExp))
		{
			throw new TypeError("Replacement object must be a string or regular expression.");
		}

		if (!Object.is(replacement, String))
		{
			throw new TypeError("Replacement must be a string.");
		}

		var result = this;

		while (result.contains(obj))
		{
			result = result.replace(obj, replacement);
		}

		return result;
	},

	/**
	 * Determines whether this string starts with a given string.
	 *
	 * @param {String} str string to test with
	 * @return {Boolean} whether this string starts with the given string
	 */
	startsWith: function (str)
	{
		if (!Object.is(str, String))
		{
			throw new TypeError("Target must be a string.");
		}

		return this.indexOf(str) == 0;
	},

	/**
	 * Converts this string to a valid JavaScript variable.
	 *
	 * @param {String} defaultVar default variable name to use if conversion fails
	 * @return {String} this string as a valid JavaScript variable or the default variable
	 */
	toValidVariable: function (defaultVar)
	{
		defaultVar = Object.is(defaultVar, String) ? defaultVar : "";

		var validVar = this.removeAll(/[^\-\$\w]/);

		while (!validVar.contains(/^[\-\$\w]/) && !validVar.isEmpty())
		{
			validVar = validVar.trimFirst();
		}

		return !validVar.isEmpty() ? validVar : defaultVar;
	},

	/**
	 * Trims the whitespace characters from the ends of this string.
	 *
	 * @return {String} trimmed string
	 */
	trim: function ()
	{
		return this.trimEnds(/\s/);
	},

	/**
	 * Trims the given objects from the end of this string.
	 *
	 * @param {String|RegExp|Array} toTrim objects to trim with
	 * @param {Number} limit maximum number of objects to trim
	 * @return {String} trimmed string
	 */
	trimEnd: function (toTrim, limit)
	{
		if (!Object.is(toTrim, String) && !Object.is(toTrim, RegExp) && !Object.is(toTrim, Array))
		{
			throw new TypeError("Object[s] to trim must be a string, regular expression, or array.");
		}

		toTrim = !Object.is(toTrim, Array) ? [ toTrim ] : toTrim;
		limit = Object.is(limit, Number) ? limit : -1;
		
		var result = this, count = 0;

		for (var a = 0; a < toTrim.length; a++)
		{
			while (!result.isEmpty() && ((limit < 0) || (count < limit)) && result.charAt(result.length - 1).contains(toTrim[a]))
			{
				result = result.substring(0, result.length - 1);

				count++;
			}
		}

		return result;
	},

	/**
	 * Removes the first character of this string.
	 *
	 * @return {String} this string with the first character removed
	 */
	trimFirst: function ()
	{
		return !this.isEmpty() ? this.substring(1, this.length) : this;
	},

	/**
	 * Trims the given objects from both ends of this string.
	 *
	 * @param {String|RegExp|Array} toTrim objects to trim with
	 * @param {Number} limit maximum number of objects to trim
	 * @return {String} trimmed string
	 */
	trimEnds: function (toTrim, limit)
	{
		if (!Object.is(toTrim, String) && !Object.is(toTrim, RegExp) && !Object.is(toTrim, Array))
		{
			throw new TypeError("Object[s] to trim must be a string, regular expression, or an array.");
		}

		return this.trimStart(toTrim, limit).trimEnd(toTrim, limit);
	},

	/**
	 * Trims the given objects from the start of this string.
	 *
	 * @param {String|RegExp|Array} toTrim objects to trim with
	 * @param {Number} limit maximum number of objects to trim
	 * @return {String} trimmed string
	 */
	trimStart: function (toTrim, limit)
	{
		if (!Object.is(toTrim, String) && !Object.is(toTrim, RegExp) && !Object.is(toTrim, Array))
		{
			throw new TypeError("Object[s] to trim must be a string, regular expression, or an array.");
		}

		toTrim = !Object.is(toTrim, Array) ? [ toTrim ] : toTrim;
		limit = Object.is(limit, Number) ? limit : -1;

		var result = this, count = 0;

		for (var a = 0; a < toTrim.length; a++)
		{
			while (!result.isEmpty() && ((limit < 0) || (count < limit)) && result.charAt(0).contains(toTrim[a]))
			{
				result = result.substring(1);

				count++;
			}
		}

		return result;
	}
});

/*===========================================================
 = BUILT-IN EXTENSION: ARRAY
 ===========================================================*/

Object.extend(Array,
/** @lends Array */
{
	/**
	 * An empty array.
	 *
	 * @constant
	 * @static
	 * @type Array
	 */
	EMPTY: [],

	/**
	 * Attempts to convert the given object into an array.
	 *
	 * @static
	 * @param {Object} obj object to convert
	 * @return {Array} converted object or an empty array
	 */
	asArray: function (obj)
	{
		if (!Object.exists(obj))
		{
			throw new TypeError("Object to convert must exist.");
		}

		if (Object.is(obj, Array))
		{
			return obj;
		}
		else if (Object.is(obj.toArray, Function))
		{
			return obj.toArray();
		}
		else if (Object.is(obj.length, Number) && obj.length)
		{
			var result = new Array(obj.length);

			for (var a = 0; a < obj.length; a++)
			{
				result[a] = Object.is(obj.item, Function) ? obj.item(a) : obj[a];
			}

			return result;
		}
		else
		{
			return [];
		}

		return arr;
	}
});

Object.extend(Array.prototype,
/** @lends Array.prototype */
{
	/**
	 * Adds an object to this array, optionally flattening any given arrays.
	 *
	 * @param {Object} obj object to add
	 * @param {Boolean} flatten optional flag for whether to flatten any given arrays
	 * @return {Object} the object that was added
	 */
	add: function (obj, flatten)
	{
		flatten = Object.is(flatten, Boolean) ? flatten : false;

		if (flatten && Object.is(obj, Array))
		{
			for (var a = 0; a < obj.length; a++)
			{
				this.push(obj[a]);
			}
		}
		else
		{
			this.push(obj);
		}

		return obj;
	},

	/**
	 * Adds all of the items in a given array to this array.
	 *
	 * @param {Array} arr array to add all items from
	 * @return {Array} this array with all items added
	 */
	addAll: function (arr)
	{
		return this.add(arr, true);
	},

	/**
	 * Clones this array by copying its items.
	 *
	 * @param {Boolean} deep whether to recursively clone this array
	 * @return {Array} clone of this array
	 */
	clone: function (deep)
	{
		deep = Object.is(deep, Boolean) ? deep : false;

		var clone = [];

		for (var a = 0; a < this.length; a++)
		{
			if (deep && Object.is(this[a], Array))
			{
				clone.push(this[a].clone(true));
			}
			else
			{
				clone.push(this[a]);
			}
		}

		return clone;
	},

	/**
	 * Determines whether this array contains a given object.
	 *
	 * @param {Object} obj object to test with
	 * @param {Number} fromIndex index to begin the search at
	 * @return {Boolean} whether this array contains the given object
	 */
	contains: function (obj, fromIndex)
	{
		if (this.indexOf)
		{
			return this.indexOf(obj, fromIndex) != -1;
		}
		else
		{
			fromIndex = Object.is(fromIndex, Number) ? fromIndex : 0;

			for (var index = fromIndex; index < this.length; index++)
			{
				if (this[index] == obj)
				{
					return true;
				}
			}
		}

		return false;
	},

	/**
	 * Gets the first element in this array or null.
	 *
	 * @return {Object} first element in this array or null
	 */
	getFirst: function ()
	{
		return !this.isEmpty() ? this[0] : null;
	},

	/**
	 * Gets the last element in this array or null.
	 *
	 * @return {Object} last element in this array or null
	 */
	getLast: function ()
	{
		return !this.isEmpty() ? this[this.length - 1] : null;
	},

	/**
	 * Determines whether this array contains no items.
	 *
	 * @return {Boolean} whether this array contains no items
	 */
	isEmpty: function ()
	{
		return this.length == 0;
	}
});

/*===========================================================
 = BUILT-IN EXTENSION: FUNCTION
 ===========================================================*/

Object.extend(Function,
/** @lends Function */
{
	/**
	 * An empty function.
	 *
	 * @constant
	 * @static
	 * @type Function
	 */
	EMPTY: function () {}
});

Object.extend(Function.prototype,
/** @lends Function.prototype */
{
	/**
	 * Binds this function to a given context.
	 *
	 * @param {Object} context context to bind to
	 * @return {Function} bound version of this function
	 */
	bind: function (context)
	{
		if (!Object.exists(context))
		{
			throw new TypeError("Bind context object must exist.");
		}

		var extraArgs = Array.asArray(arguments).slice(1);
		var method = this;

		return function ()
		{
			var args = Array.asArray(arguments);
			args.addAll(extraArgs);

			return method.apply(context, args);
		};
	},

	/**
	 * Binds this function to a given context as an event by making sure the first argument is the event object.
	 *
	 * @param {Object} context context to bind to
	 * @return {Function} bound version of this function
	 */
	bindAsEvent: function (context)
	{
		if (!Object.exists(context))
		{
			throw new TypeError("Bind context object must exist.");
		}

		var extraArgs = Array.asArray(arguments).slice(1);
		var method = this;

		return function ()
		{
			var args = [];

			if (Object.exists(window.event))
			{
				args.push(window.event);
			}

			args.addAll(Array.asArray(arguments));
			args.addAll(extraArgs);

			return method.apply(context, args);
		};
	}
});

/*===========================================================
 = ELEMENTIMPL
 ===========================================================*/

var ElementImpl = Class.create("ElementImpl");

Object.extend(ElementImpl,
/** @lends ElementImpl */
{
	/**
	 * Name of the property to use for the element tag name.
	 *
	 * @constant
	 * @static
	 * @type String
	 */
	TAG_NAME_PROPERTY_NAME: "__tag",

	/**
	 * Prefix for attribute property names.
	 *
	 * @constant
	 * @static
	 * @type String
	 */
	ATTRIBUTE_PROPERTY_NAME_PREFIX: "_",

	/**
	 * TODO: document
	 */
	build: function (data)
	{
		if (!Object.exists(data))
		{
			throw new TypeError("Element data object must exist.");
		}

		if (!Object.is(data[ElementImpl.TAG_NAME_PROPERTY_NAME], String))
		{
			throw new TypeError("Element tag name must be a string: " + data[ElementImpl.TAG_NAME_PROPERTY_NAME]);
		}

		var tagName = data[ElementImpl.TAG_NAME_PROPERTY_NAME];

		if (tagName.isEmpty())
		{
			throw new Error("Element tag name must not be empty.");
		}
		
		var element = document.createElement(tagName);

		for (var property in data)
		{
			if (data.hasOwnProperty(property) && (property != ElementImpl.TAG_NAME_PROPERTY_NAME))
			{
				var value = data[property];

				if (property.startsWith(ElementImpl.ATTRIBUTE_PROPERTY_NAME_PREFIX))
				{
					element.setAttribute(property.trimStart(ElementImpl.ATTRIBUTE_PROPERTY_NAME_PREFIX), value);
				}
				else if (!Object.exists(value))
				{
					throw new Error("Element data values must not be null: " + property);
				}
				else if (Object.is(value, String) || Object.is(value, Number) || Object.is(value, Boolean))
				{
					element.appendChild(document.createTextNode(value));
				}
				else if (Object.is(value, Function))
				{
					element.appendChild(value());
				}
				else if (Object.is(value, Object))
				{
					element.appendChild(ElementImpl.build(value));
				}
				else
				{
					throw new TypeError("Unknown element data value type: " + (typeof value));
				}
			}
		}

		return element;
	}
});

/*===========================================================
 = POPUPSIZE
 ===========================================================*/

var PopupSize = Class.create("PopupSize");

Object.extend(PopupSize.prototype,
/** @lends PopupSize.prototype */
{
	/**
	 * Name of this popup size.
	 *
	 * @type String
	 */
	name: undefined,

	/**
	 * Options for this popup size.
	 *
	 * @type Object
	 */
	options: undefined,

	/**
	 * @class Enumeration of pre-defined popup sizes.
	 * @exports PopupSize#__construct as PopupSize
	 *
	 * @param {String} name name of the popup size
	 * @param {Object} options options for the popup size
	 */
	__construct: function (name, options)
	{
		this.name = name;
		this.options = Object.is(options, Object) ? options : {};
	},

	getName: function ()
	{
		return this.name;
	},

	getOptions: function ()
	{
		return this.options;
	}
});

Object.extend(PopupSize,
/** @lends PopupSize */
{
	/**
	 * Normal popup size.
	 *
	 * @constant
	 * @static
	 * @type PopupSize
	 */
	NORMAL: new PopupSize("normal",
	{
		width: 880,
		height: 500
	}),

	/**
	 * Medium popup size.
	 *
	 * @constant
	 * @static
	 * @type PopupSize
	 */
	MEDIUM: new PopupSize("medium",
	{
		width: 500,
		height: 550
	}),

	/**
	 * Small popup size.
	 *
	 * @constant
	 * @static
	 * @type PopupSize
	 */
	SMALL: new PopupSize("small",
	{
		width: 300,
		height: 300
	}),

	/**
	 * Mini popup size.
	 *
	 * @constant
	 * @static
	 * @type PopupSize
	 */
	MINI: new PopupSize("mini",
	{
		width: 200,
		height: 200
	})
});

/*===========================================================
 = BUILT-IN EXTENSION: WINDOW (GLOBAL)
 ===========================================================*/

/** @class */
var Window = Object.exists(window.Window) ? window.Window : window;

Object.extend(window,
/** @lends Window */
{
	/**
	 * Window options common to all browsers.
	 *
	 * @constant
	 * @static
	 * @type Array
	 */
	COMMON_WINDOW_OPTION_KEYS:
	[
		"left", "top", "width", "height", "toolbar", "location", "directories", "status",
		"menubar", "scrollbars", "resizable"
	],

	/**
	 * Window options specific to IE-based browsers.
	 *
	 * @constant
	 * @static
	 * @type Array
	 */
	IE_WINDOW_OPTION_KEYS:
	[
		"channelmode", "fullscreen"
	],

	/**
	 * Window options specific to Netscape-based browsers.
	 *
	 * @constant
	 * @static
	 * @type Array
	 */
	NETSCAPE_WINDOW_OPTION_KEYS:
	[
		"screenX", "screenY"
	],

	/**
	 * Default popup window options.
	 *
	 * @constant
	 * @static
	 * @type Object
	 */
	DEFAULT_POPUP_WINDOW_OPTIONS:
	{
		baseName: "popup",

		x: 20,
		y: 20,
		width: PopupSize.NORMAL.getOptions().width,
		height: PopupSize.NORMAL.getOptions().height,
		toolbar: true,
		location: false,
		directories: false,
		status: true,
		menubar: true,
		scrollbars: true,
		resizable: true,
		record: false,

		// IE options
		channelmode: false,
		fullscreen: false
	},

	/**
	 * Array of popups that this window has opened.
	 * 
	 * @type Array
	 */
	popups: [],

	/**
	 * TODO: document
	 */
	cleanPopups: function ()
	{
		var cleanedPopups = [];

		for (var a = 0; a < popups.length; a++)
		{
			var popup = popups[a];

			if (Object.is(popup.closed, Boolean) && !popup.closed)
			{
				cleanedPopups.push(popup);
			}
		}

		popups = cleanedPopups;
	},

	/**
	 * TODO: document
	 */
	closePopups: function ()
	{
		cleanPopups();

		for (var a = 0; a < popups.length; a++)
		{
			popups[a].close();
		}
	},

	/**
	 * Opens a popup window for a given URL and options.
	 *
	 * @static
	 * @param {String|URL} url URL to use
	 * @param {Object} options optional options for the popup
	 * @param {Window} the popup window
	 */
	popup: function (url, options)
	{
		url = Object.is(url, URL) ? url.getURL() : url;

		if (!Object.is(url, String))
		{
			throw new TypeError("Popup URL must be a string.");
		}

		// Validating/building options
		options = Object.is(options, Object) ? options : {};

		// Validating name
		options.name = Object.is(options.name, String) ? options.name : (DEFAULT_POPUP_WINDOW_OPTIONS.baseName + popups.length);

		if (!options.name.isValidVariable())
		{
			console.warn("Popup window name must not contain non-variable characters: " + options.name);

			options.name = options.name.toValidVariable();

			if (!options.name.isEmpty())
			{
				console.info("Transformed popup window name into valid variable: " + options.name);
			}
			else
			{
				options.name = DEFAULT_POPUP_WINDOW_OPTIONS.baseName + popups.length;

				console.info("Unable to transform popup window name into valid variable - using default: " + options.name);
			}
		}

		if (Object.is(options.size, String))
		{
			options.size = options.size.trim().toUpperCase();

			options.size = PopupSize[options.size];

			if (!Object.is(options.size, PopupSize))
			{
				console.warn("Unknown popup window size specified: " + options.size);
			}
		}

		if (Object.is(options.size, PopupSize))
		{
			console.debug("Setting popup size: name=" + options.size.getName() + ", options=" + options.size.getOptions());

			Object.extend(options, options.size.getOptions());
		}

		options.x = Object.is(options.x, Number) ? options.x : DEFAULT_POPUP_WINDOW_OPTIONS.x;
		options.y = Object.is(options.y, Number) ? options.y : DEFAULT_POPUP_WINDOW_OPTIONS.y;
		options.width = Object.is(options.width, Number) ? options.width : DEFAULT_POPUP_WINDOW_OPTIONS.width;
		options.height = Object.is(options.height, Number) ? options.height : DEFAULT_POPUP_WINDOW_OPTIONS.height;
		options.toolbar = Object.is(options.toolbar, Boolean) ? options.toolbar : DEFAULT_POPUP_WINDOW_OPTIONS.toolbar;
		options.location = Object.is(options.location, Boolean) ? options.location : DEFAULT_POPUP_WINDOW_OPTIONS.location;
		options.directories = Object.is(options.directories, Boolean) ? options.directories : DEFAULT_POPUP_WINDOW_OPTIONS.directories;
		options.status = Object.is(options.status, Boolean) ? options.status : DEFAULT_POPUP_WINDOW_OPTIONS.status;
		options.menubar = Object.is(options.menubar, Boolean) ? options.menubar : DEFAULT_POPUP_WINDOW_OPTIONS.menubar;
		options.scrollbars = Object.is(options.scrollbars, Boolean) ? options.scrollbars : DEFAULT_POPUP_WINDOW_OPTIONS.scrollbars;
		options.resizable = Object.is(options.resizable, Boolean) ? options.resizable : DEFAULT_POPUP_WINDOW_OPTIONS.resizable;
		options.record = Object.is(options.record, Boolean) ? options.record : DEFAULT_POPUP_WINDOW_OPTIONS.record;

		// Populating option aliases
		options.left = Object.is(options.left, Number) ? options.left : options.x;
		options.top = Object.is(options.top, Number) ? options.top : options.y;
		options.replace = options.record;

		console.debug("Using popup window options: " + options);

		// Building option keys
		var optionKeys = COMMON_WINDOW_OPTION_KEYS.clone();

		if (Browser.isIE())
		{
			options.channelmode = Object.is(options.channelmode, Boolean) ? options.channelmode : DEFAULT_POPUP_WINDOW_OPTIONS.channelmode;
			options.fullscreen = Object.is(options.fullscreen, Boolean) ? options.fullscreen : DEFAULT_POPUP_WINDOW_OPTIONS.fullscreen;

			optionKeys.addAll(IE_WINDOW_OPTION_KEYS);
		}

		if (Browser.isNetscapeBased())
		{
			options.screenX = options.left;
			options.screenY = options.top;

			optionKeys.addAll(NETSCAPE_WINDOW_OPTION_KEYS);
		}

		console.debug("Using popup window option keys: " + optionKeys);

		// Building features string
		var features = "";

		for (var a = 0; a < optionKeys.length; a++)
		{
			var optionKey = optionKeys[a];

			if (Object.exists(options[optionKey]))
			{
				features += (!features.isEmpty() ? "," : "") + optionKey + "=" +
					(Object.is(options[optionKey], Boolean) ? options[optionKey].toNumber() : options[optionKey]);
			}
			else
			{
				console.warn("Popup window option is not set: " + optionKey);
			}
		}

		console.info("Opening popup: url=" + url, ", name=" + options.name + ", features=" + features);

		var popupWindow = open(url, options.name, features, options.replace);

		storePopup(popupWindow);

		if (Object.is(options.returnPopup, Boolean) && options.returnPopup)
		{
			return popupWindow;
		}
	},

	/**
	 * Opens a popup window for with the given text and options.
	 *
	 * @static
	 * @param {String} text text to use
	 * @param {Object} options optional options for the popup
	 * @param {Window} the popup window
	 */
	popupText: function (text, options)
	{
		options = Object.is(options, Object) ? options : {};

		var originalReturnPopup = options.returnPopup;
		options.returnPopup = true;

		var popupWindow = popup("_blank", options);

		write(popupWindow, text);
		
		if (Object.is(originalReturnPopup, Boolean) && originalReturnPopup)
		{
			return popupWindow;
		}
	},

	/**
	 * Sets a timer in the window.
	 *
	 * @static
	 * @param {Function} func function to call when the interval has elapsed
	 * @param {Number} interval interval to set the timer for, in milliseconds
	 * @param {Boolean} continuous optional flag for whether the timer will continue to reset and start until stopped
	 * @param {Array} args optional arguments to pass to the timer's function
	 * @return {Number} id of the created timer
	 */
	setTimer: function (func, interval, continuous, args)
	{
		if (continuous)
		{
			return window.setInterval(func, interval, args);
		}
		else
		{
			return window.setTimeout(func, interval, args);
		}
	},

	/**
	 * TODO: document
	 */
	sleep: function (interval)
	{
		if (!Object.is(interval, Number))
		{
			throw new TypeError("Sleep interval must be a number.");
		}

		if (interval < 0)
		{
			throw new Error("Sleep interval must not be negative.");
		}

		var time = new Date().getTime();

		while (new Date().getTime() < (time + interval))
		{
		}
	},

	/**
	 * Stores a given popup in this window for future use.
	 *
	 * @static
	 * @param {Window} popup popup window to store
	 */
	storePopup: function (popup)
	{
		cleanPopups();

		console.debug("Storing popup: name=" + popup.name + ", url=" + (Object.exists(popup.document) ? popup.document.location.href : "<undefined>"));

		popups.push(popup);
	},

	/**
	 * Writes text to a given window.
	 *
	 * @static
	 * @param {Window} win window to write to
	 * @param {String} text string to write
	 */
	write: function (win, text)
	{
		text = text.replaceAll("&quot;", "\"").replaceAll("&apos;", "\'");

		win.document.open();
		win.document.write(text);
		win.document.close();
	}
});

/*===========================================================
 = BUILT-IN EXTENSION: DOCUMENT (GLOBAL)
 ===========================================================*/

/** @class */
var Document = Object.exists(window.Document) ? window.Document : document;

Object.extend(document,
/** @lends Document */
{
	/**
	 * TODO: document
	 */
	getHead: function ()
	{
		var headElements = $$("head");

		return Object.exists(headElements) && !headElements.isEmpty() ? headElements.getFirst() : null;
	},

	/**
	 * TODO: document
	 */
	getBody: function ()
	{
		var bodyElements = $$("body");

		return Object.exists(bodyElements) && !bodyElements.isEmpty() ? bodyElements.getFirst() : null;
	},

	/**
	 * TODO: document
	 */
	getLocation: function ()
	{
		return this.location.href;
	},

	/**
	 * TODO: document
	 */
	redirect: function (url)
	{
		this.location.href = url;
	}
});

/*===========================================================
 = GLOBAL FUNCTIONS
 ===========================================================*/

/**
 * Selects a single element via a given query.
 *
 * @param {String} query query to select with
 * @param {Element} parentNode optional parent node to select from
 * @return {Element} selected element or null if not found
 */
function $(query, parentNode)
{
	if (!Object.is(query, String))
	{
		throw new TypeError("Element query must be a string.");
	}

	query = query.trim();
	parentNode = Object.exists(parentNode) ? parentNode : document;

	if (parentNode.querySelector)
	{
		return parentNode.querySelector(query);
	}
	else
	{
		console.debug("Element.prototype.querySelector is not available.");

		if (!query.contains(/\s/))
		{
			if (query.startsWith("#"))
			{
				return document.getElementById(query.trimStart("#", 1));
			}
			else
			{
				return $$(query, parentNode).getFirst();
			}
		}
		else
		{
			console.error("Element query is not a valid ID or tag name: " + query);

			return null;
		}
	}
}

/**
 * Selects elements via a given query.
 *
 * @param {String} query query to select with
 * @param {Element} parentNode optional parent node to select from
 * @return {Array} selected elements or an empty array if not found
 */
function $$(query, parentNode)
{
	if (!Object.is(query, String))
	{
		throw new TypeError("Element query must be a string.");
	}

	query = query.trim();
	parentNode = Object.exists(parentNode) ? parentNode : document;

	if (parentNode.querySelectorAll)
	{
		return Array.asArray(parentNode.querySelectorAll(query));
	}
	else
	{
		console.debug("Element.prototype.querySelector is not available.");

		if (!query.contains(/\s/))
		{
			return Array.asArray(document.getElementsByTagName(query));
		}
		else
		{
			console.error("Element query is not a valid tag name: " + query);

			return [];
		}
	}
}

/*===========================================================
 = BROWSER
 ===========================================================*/

/**
 * @class A wrapper for the user's browser.
 */
var Browser = Class.create("Browser");

Object.extend(Browser,
/** @lends Browser */
{
	/**
	 * TODO: document
	 */
	getName: function ()
	{
		if (Browser.isFirefox())
		{
			return "Firefox";
		}
		else if (Browser.isIE())
		{
			return "IE";
		}
		else if (Browser.isChrome())
		{
			return "Chrome";
		}
		else if (Browser.isSafari())
		{
			return "Safari";
		}
		else
		{
			return "<unknown>";
		}
	},

	/**
	 * TODO: document
	 */
	getUserAgent: function ()
	{
		return navigator.userAgent;
	},

	/**
	 * TODO: document
	 */
	getVersion: function ()
	{
		var version = undefined;

		if (Browser.isFirefox())
		{
			version = Browser.getUserAgent().match(/Firefox\/([\d\.]+)/)[1];
		}
		else if (Browser.isIE())
		{
			version = Browser.getUserAgent().match(/MSIE\s([\d\.]+);/)[1];
		}
		else if (Browser.isChrome())
		{
			version = Browser.getUserAgent().match(/Chrome\/([\d\.]+)/)[1];
		}
		else if (Browser.isSafari())
		{
			version = Browser.getUserAgent().match(/Safari\/([\d\.]+)/)[1];
		}

		if (!Object.isUndefined(version))
		{
			version = version.replace(".", "|").replaceAll(".", "").replace("|", ".");

			return parseFloat(version);
		}
		else
		{
			return 0;
		}
	},

	/**
	 * TODO: document
	 */
	isChrome: function ()
	{
		return Browser.getUserAgent().contains("Chrome");
	},

	/**
	 * TODO: document
	 */
	isFirefox: function ()
	{
		return Browser.getUserAgent().contains("Firefox");
	},

	/**
	 * TODO: document
	 */
	isIE: function ()
	{
		return Browser.getUserAgent().contains("MSIE");
	},

	/**
	 * TODO: document
	 */
	isMajorVersion: function (majorVersion)
	{
		return (Browser.getVersion() >= majorVersion) && (Browser.getVersion() < (majorVersion + 1));
	},

	/**
	 * TODO: document
	 */
	isNetscapeBased: function ()
	{
		return navigator.appName == "Netscape";
	},

	/**
	 * TODO: document
	 */
	isSafari: function ()
	{
		return Browser.getUserAgent().contains("Safari");
	}
});

/*===========================================================
 = TIMER
 ===========================================================*/

var Timer = Class.create("Timer");

Object.extend(Timer.prototype,
/** @lends Timer.prototype */
{
	/**
	 * Function to call once the interval has elapsed.
	 *
	 * @type Function
	 */
	func: undefined,

	/**
	 * Interval to wait for, in milliseconds.
	 *
	 * @type Number
	 */
	interval: undefined,

	/**
	 * Optional arguments to pass to the timer's function.
	 *
	 * @type Array
	 */
	args: undefined,

	/**
	 * Whether the timer is continuous (i.e. uses setInterval).
	 *
	 * @type Boolean
	 */
	continuous: undefined,

	/**
	 * Whether to automatically start the timer.
	 *
	 * @type Boolean
	 */
	autoStart: undefined,

	/**
	 * ID of the timer.
	 *
	 * @type Number
	 */
	id: undefined,

	/**
	 * @class A wrapper for the setTimeout and setInterval global functions.
	 * @exports Timer#__construct as Timer
	 *
	 * @param {String} func function to call once the interval has elapsed
	 * @param {Number} interval interval to wait for, in milliseconds
	 * @param {Array} args optional arguments to pass to the timer's function
	 * @param {Boolean} continuous optional flag for whether the timer is continuous (i.e. uses setInterval)
	 * @param {Boolean} autoStart optional flag for whether to automatically start the timer
	 */
	__construct: function (func, interval, args, continuous, autoStart)
	{
		this.func = func;
		this.interval = interval;
		this.args = Object.exists(args) ? args : [];
		this.continuous = Object.exists(continuous) ? continuous : false;
		this.autoStart = Object.exists(autoStart) ? autoStart : true;

		this.initTimer();
	},

	/**
	 * Starts the timer if it is not already started.
	 */
	start: function ()
	{
		if (!Object.exists(this.id))
		{
			var flatArgs = new Array(this.func, this.interval, this.continuous);
			flatArgs.addAll(this.args);

			this.id = window.setTimer.apply(window, flatArgs);

			console.debug("Started timer: id=" + this.id);
		}
		else
		{
			console.info("Timer is already started: id=" + this.id);
		}
	},

	/**
	 * Stops the timer if it is started.
	 */
	stop: function ()
	{
		if (Object.exists(this.id))
		{
			if (this.continuous)
			{
				window.clearInterval(this.id);
			}
			else
			{
				window.clearTimeout(this.id);
			}

			console.debug("Stopped timer: id=" + this.id);

			this.id = undefined;
		}
		else
		{
			console.info("Timer is not started.");
		}
	},

	/**
	 * Initializes the timer.
	 */
	initTimer: function ()
	{
		if (!Object.is(this.func, Function))
		{
			throw new TypeError("Timer function must be a function.");
		}

		if (!Object.is(this.interval, Number))
		{
			throw new TypeError("Timer interval must be a number.");
		}

		if (!Object.is(this.args, Array))
		{
			throw new TypeError("Timer function arguments must be an array.");
		}

		if (!Object.is(this.continuous, Boolean))
		{
			throw new TypeError("Continuous flag must be a boolean.");
		}

		if (!Object.is(this.autoStart, Boolean))
		{
			throw new TypeError("Auto start flag must be a boolean.");
		}

		if (this.autoStart)
		{
			this.start();
		}
	}
});

/*===========================================================
 = URL
 ===========================================================*/

var URL = Class.create("URL");

Object.extend(URL,
/** @lends URL */
{
	/**
	 * Pattern for URLs.
	 *
	 * @constant
	 * @static
	 * @type RegExp
	 */
	URL_PATTERN: /^([^:]+):\/\/([^\/]+)(\/[^\?]*)\??(([^=]+=[^&]*&?)*)$/,

	/**
	 * Pattern for URL hosts.
	 *
	 * @constant
	 * @static
	 * @type RegExp
	 */
	HOST_PATTERN: /^([^:]+):?(\d*)$/,

	/**
	 * Pattern for individual URL query parameters.
	 *
	 * @constant
	 * @static
	 * @type RegExp
	 */
	PARAM_PATTERN: /^([^=]+)=([^&]*)&?$/
});

Object.extend(URL.prototype,
/** @lends URL.prototype */
{
	/**
	 * URL to use.
	 *
	 * @type String
	 */
	url: undefined,

	/**
	 * @class A wrapper for URLs.
	 * @exports URL#__construct as URL
	 *
	 * @param {String} url url string to use
	 */
	__construct: function (url)
	{
		this.url = url;
	},

	/**
	 * Determines whether this URL has query parameters.
	 *
	 * @return {Boolean} whether this URL has query parameters
	 */
	hasParams: function ()
	{
		return !this.getParams().isEmpty();
	},

	/**
	 * Determines whether this URL has a port specification.
	 *
	 * @return {Boolean} whether this URL has a port specification
	 */
	hasPort: function ()
	{
		return !this.getPort().isEmpty();
	},

	/**
	 * Gets the path of this URL.
	 *
	 * @return {String} path of this URL or an empty string if it is not valid
	 */
	getPath: function ()
	{
		return this.isValid() ? this.getParts()[2] : "";
	},

	/**
	 * Gets the query parameters of this URL.
	 *
	 * @return {Array} query parameters of this URL or an empty array if it is not valid
	 */
	getParams: function ()
	{
		if (this.isValid())
		{
			var parts = this.getParts();

			return (parts.length > 4) ? parts.slice(4) : [];
		}
		else
		{
			return [];
		}
	},

	/**
	 * Gets the port specification of this URL.
	 *
	 * @return {String} port specification of this URL or an empty string if it is not valid
	 */
	getPort: function ()
	{
		if (this.isValid())
		{
			var hostParts = URL.HOST_PATTERN.exec(this.getParts()[1]);

			return (hostParts.length > 1) ? hostParts[2] : "";
		}
		else
		{
			return "";
		}
	},

	/**
	 * Gets the host of this URL.
	 *
	 * @return {String} host of this URL or an empty string if it is not valid
	 */
	getHost: function ()
	{
		return this.isValid() ? URL.HOST_PATTERN.exec(this.getParts()[1])[1] : "";
	},

	/**
	 * Gets the protocol of this URL.
	 *
	 * @return {String} protocol of this URL or an empty string if it is not valid
	 */
	getProtocol: function ()
	{
		return this.isValid() ? this.getParts()[0] : "";
	},

	/**
	 * Gets the parts of this URL.
	 *
	 * @return {Array} parts of this URL or an empty array if it is not valid
	 */
	getParts: function ()
	{
		if (this.isValid())
		{
			var parts = URL.URL_PATTERN.exec(this.url);

			return parts.slice(1);
		}
		else
		{
			return [];
		}
	},

	/**
	 * TODO: document
	 */
	getURL: function ()
	{
		return this.url;
	},

	/**
	 * Determines whether this URL is valid.
	 *
	 * @return {Boolean} whether this URL is valid
	 */
	isValid: function ()
	{
		return URL.URL_PATTERN.test(this.url);
	}
});

/*===========================================================
 = AJAX
 ===========================================================*/

var AjaxRequestMethod = Class.create("AjaxRequestMethod");

Object.extend(AjaxRequestMethod.prototype,
/** @lends AjaxRequestMethod.prototype */
{
	/**
	 * TODO: document
	 */
	method: undefined,

	/**
	 * @class An enumeration of XMLHttpRequest methods.
	 * @exports AjaxRequestMethod#__construct as AjaxRequestMethod
	 *
	 * @param {String} method method name
	 */
	__construct: function (method)
	{
		this.method = method;
	},

	/**
	 * TODO: document
	 */
	getMethod: function ()
	{
		return this.method;
	}
});

Object.extend(AjaxRequestMethod,
/** @lends AjaxRequestMethod */
{
	/**
	 * TODO: document
	 */
	GET: new AjaxRequestMethod("GET"),

	/**
	 * TODO: document
	 */
	POST: new AjaxRequestMethod("POST")
});

var AjaxRequestReadyState = Class.create("AjaxRequestReadyState");

Object.extend(AjaxRequestReadyState.prototype,
/** @lends AjaxRequestReadyState.prototype */
{
	/**
	 * TODO: document
	 */
	readyState: undefined,

	/**
	 * @class An enumeration of XMLHttpRequest ready states.
	 * @exports AjaxRequestReadyState#__construct as AjaxRequestReadyState
	 *
	 * @param {Number} readyState numerical representation of the ready state
	 */
	__construct: function (readyState)
	{
		this.readyState = readyState;
	},

	/**
	 * TODO: document
	 */
	getReadyState: function ()
	{
		return this.readyState;
	}
});

Object.extend(AjaxRequestReadyState,
/** @lends AjaxRequestReadyState */
{
	/**
	 * TODO: document
	 */
	UNINITIALIZED: new AjaxRequestReadyState(0),

	/**
	 * TODO: document
	 */
	LOADING: new AjaxRequestReadyState(1),

	/**
	 * TODO: document
	 */
	LOADED: new AjaxRequestReadyState(2),

	/**
	 * TODO: document
	 */
	INTERACTIVE: new AjaxRequestReadyState(3),
	
	/**
	 * TODO: document
	 */
	COMPLETE: new AjaxRequestReadyState(4)
});

var AjaxRequestStatus = Class.create("AjaxRequestStatus");

Object.extend(AjaxRequestStatus.prototype,
/** @lends AjaxRequestStatus.prototype */
{
	/**
	 * TODO: document
	 */
	status: undefined,

	/**
	 * @class An enumeration of XMLHttpRequest statuses.
	 * @exports AjaxRequestStatus#__construct as AjaxRequestStatus
	 *
	 * @param {Number} status numerical representation of the status
	 */
	__construct: function (status)
	{
		this.status = status;
	},

	/**
	 * TODO: document
	 */
	getStatus: function ()
	{
		return this.status;
	}
});

Object.extend(AjaxRequestStatus,
/** @lends AjaxRequestStatus */
{
	/**
	 * TODO: document
	 */
	OK: new AjaxRequestStatus(200),

	/**
	 * TODO: document
	 */
	NOT_FOUND: new AjaxRequestStatus(404)
});

var AjaxRequest = Class.create("AjaxRequest");

Object.extend(AjaxRequest,
/** @lends AjaxRequest */
{
	/**
	 * TODO: document
	 */
	DEFAULT_METHOD: AjaxRequestMethod.GET.getMethod(),

	/**
	 * TODO: document
	 */
	newRequest: function ()
	{
		if (Object.exists(window.XMLHttpRequest))
		{
			return new XMLHttpRequest();
		}
		else if (Object.exists(window.ActiveXObject))
		{
			return new ActiveXObject("Msxml2.XMLHTTP");
		}
		else
		{
			throw new Error("Unable to instantiate a XML HTTP request object.");
		}
	}
});

Object.extend(AjaxRequest.prototype, 
/** @lends AjaxRequest.prototype */
{
	/**
	 * TODO: document
	 */
	url: undefined,

	/**
	 * TODO: document
	 */
	options: undefined,

	/**
	 * TODO: document
	 */
	request: undefined,

	/**
	 * @class A wrapper for XMLHttpRequests.
	 * @exports AjaxRequest#__construct as AjaxRequest
	 *
	 * @param {String} url url to request
	 * @param {Object} options optional options for the request
	 */
	__construct: function (url, options)
	{
		this.url = url;
		this.options = Object.is(options, Object) ? options : {};

		this.initRequest();
	},

	/**
	 * TODO: document
	 */
	send: function ()
	{
		if (Object.exists(this.request))
		{
			this.request.send(this.options.data);
		}
	},

	/**
	 * TODO: document
	 */
	hasRequest: function ()
	{
		return Object.exists(this.request);
	},

	/**
	 * TODO: document
	 */
	getRequest: function ()
	{
		return this.request;
	},

	/**
	 * TODO: document
	 */
	hasResponse: function ()
	{
		return this.hasResponseText() || this.hasResponseXML();
	},

	/**
	 * TODO: document
	 */
	hasResponseText: function ()
	{
		return !Object.isNull(this.getResponseText());
	},

	/**
	 * TODO: document
	 */
	getResponseText: function ()
	{
		return Object.exists(this.request) ? this.request.responseText : null;
	},

	/**
	 * TODO: document
	 */
	hasResponseXML: function ()
	{
		return !Object.isNull(this.getResponseXML());
	},

	/**
	 * TODO: document
	 */
	getResponseXML: function ()
	{
		return Object.exists(this.request) ? this.request.responseXML : null;
	},

	/**
	 * TODO: document
	 */
	hasResponseJSON: function ()
	{
		return !Object.isNull(this.getResponseJSON());
	},

	/**
	 * TODO: document
	 */
	getResponseJSON: function ()
	{
		return this.hasResponseXML ? Object.fromXMLDocument(this.getResponseXML()) : {};
	},

	/**
	 * TODO: document
	 */
	initRequest: function ()
	{
		this.options.method = Object.is(this.options.method, String) ? this.options.method : AjaxRequest.DEFAULT_METHOD;
		this.options.data = Object.is(this.options.data, String) ? this.options.data :
			((this.options.method == AjaxRequestMethod.GET.getMethod()) ? null : "");
		this.options.onReadyStateChange = Object.is(this.options.onReadyStateChange, Function) ? this.options.onReadyStateChange : null;
		this.options.onSuccess = Object.is(this.options.onSuccess, Function) ? this.options.onSuccess : null;
		this.options.onFailure = Object.is(this.options.onFailure, Function) ? this.options.onFailure : null;
		this.options.autoSend = Object.is(this.options.autoSend, Boolean) ? this.options.autoSend : false;

		this.request = AjaxRequest.newRequest();
		this.request.open(this.options.method, this.url);

		if (Object.exists(this.options.onReadyStateChange))
		{
			this.request.onreadystatechange = this.options.onReadyStateChange;
		}

		if (this.options.autoSend)
		{
			this.send();
		}
	}
});