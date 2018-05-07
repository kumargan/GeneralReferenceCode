exports.handler = (events, context) => {

  console.log(events)
  var Slack = require('node-slackr');
  console.log('env is',process.env.slack_webhook_url);
 var slack = new Slack(process.env.slack_webhook_url,{
      channel: "#360_view_events",
       username: "Server Notifications",
       icon_url: "https://www.google.co.in/imgres?imgurl=https%3A%2F%2Fimage.slidesharecdn.com%2Fw5jxenheqvmb1ci4llaq-signature-9d57fa9c7d324166570cd06f6b5d3e2dac908dbe8373338701be7e2d36406a0c-poli-150514161033-lva1-app6891%2F95%2Foperating-opnfv-deploy-it-test-it-run-it-11-638.jpg%3Fcb%3D1431621055&imgrefurl=https%3A%2F%2Fwww.slideshare.net%2FOPNFV%2F5-nfv-wc-operatingopnfvbrockners-final&docid=hyqAeGVWzMGvxM&tbnid=TVo3ac_71EBgiM%3A&vet=10ahUKEwihme333vfWAhUESo8KHUdNAvsQMwjkASgVMBU..i&w=638&h=359&bih=930&biw=1676&q=deploy&ved=0ahUKEwihme333vfWAhUESo8KHUdNAvsQMwjkASgVMBU&iact=mrc&uact=8",
      icon_emoji: ":robot_face:"
     });

    // events = JSON.parse(events);

    var newMsg = "Subject : "+ events.Records[0].Sns.Subject+"\n Message : " +events.Records[0].Sns.Message;
    
    slack.notify(newMsg, function(err, result){
    console.log(err,result);
    });

};