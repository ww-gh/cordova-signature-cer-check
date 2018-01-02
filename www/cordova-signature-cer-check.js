var exec = require('cordova/exec');

exports.checkSignature = function(arg0, success, error) {
    exec(success, error, "SignatureCerCheck", "checkSignature", [arg0]);
};

exports.getSignatureHashCode = function(success, error) {
	exec(success, error, "SignatureCerCheck", "getSignatureHashCode", []);
};

exports.getSignatureInfo = function(success, error) {
	exec(success, error, "SignatureCerCheck", "getSignatureInfo", []);
};

exports.getSignatureSHA1 = function(success, error) {
	exec(success, error, "SignatureCerCheck", "getSignatureSHA1", []);
};