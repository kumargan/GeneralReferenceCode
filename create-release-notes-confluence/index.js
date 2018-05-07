var notes = require("./releasenotes.js");
var AWS = require('aws-sdk');

exports.handler = (event, context, callback) => {

    console.log('\n\n === Release Notes === \n\n');
    
    var gitToken = process.env.GIT_TOKEN;
    var wikiUser = process.env.WIKI_USER;
    var wikiPass = process.env.WIKI_PASS;
    
    var codepipeline = new AWS.CodePipeline();
    var jobId = event["CodePipeline.job"].id;
    
    // Notify AWS CodePipeline of a successful job
    var putJobSuccess = function(message) {
        var params = {
            jobId: jobId
        };
        codepipeline.putJobSuccessResult(params, function(err, data) {
            if(err) {
                context.fail(err);      
            } else {
                context.succeed(message);      
            }
        });
    };
      
    // Notify AWS CodePipeline of a failed job
    var putJobFailure = function(message) {
        var params = {
            jobId: jobId,
            failureDetails: {
                message: JSON.stringify(message),
                type: 'JobFailed',
                externalExecutionId: context.invokeid
            }
        };
        codepipeline.putJobFailureResult(params, function(err, data) {
            context.fail(message);      
        });
    };
    
  try {
            console.log('\n\n === Create Notes === \n\n');
            notes.create_notes(gitToken,wikiUser,wikiPass);
            putJobSuccess("got release notes");
        } catch (ex) {
            console.log('\n\n === Error === \n\n');
            putJobFailure(ex);  
        }

};