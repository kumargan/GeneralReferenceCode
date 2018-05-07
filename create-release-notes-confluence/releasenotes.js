var https = require('https');
var github = require('octonode');

var getLatestTags = function (ghrepo, callback) {
    var latest_tag = '';
    var previous_tag = '';

    ghrepo.tags((err, tags) => {
      if (err) {
        console.log(tags);
        console.log(err);
      } else {
        var two_tags = tags.slice(0, 2);
        console.log('Got Latest Tags');
        console.log(two_tags);
        callback(two_tags);
      }
    });
};


var getLatestCommits = function (ghrepo, callback) {
  getLatestTags(ghrepo, function (two_tags) {
    var results = [];
    latest_tag = two_tags[0].name;
    previous_tag = two_tags[1].name;
    results['tags'] = two_tags;

    getCompare(ghrepo, previous_tag, latest_tag, function (commits) {
        callback(commits, two_tags);
    });
  });
};

var getCompare = function (ghrepo, previous_tag, latest_tag, callback) {
    ghrepo.compare(previous_tag, latest_tag, (err, data) => {
      if (err) {
        console.log(err);
      } else {
        var commits = data.commits;
        var results = [];
        for (var commit in commits) {
            var commit_id = commits[commit].sha;
            results.push(commit_id);
        }
        callback(results);
      }
    });
};

var getCommit = function (ghrepo, commit_id) {

    return new Promise((resolve, reject) => {
        ghrepo.commit(commit_id, function (err, data) {
        if (!err) {
            var commit_message = data.commit.message;
            resolve(commit_message);
          } else {
            reject(err);
          }
        });
    })
};

var getReleaseDetails = function (ghrepo, callback) {
ghrepo.releases(function(err, releases, headers){
    if(err){
      console.log('err', err);
      return callback(err);
    }
    var release = releases[0];
    callback(null, release.name, release.body, release.published_at);
  });
}

var createConfluence = function (page_content, release, tags, login) {
    var notes_title = "release notes - tag:" + tags[0].name;
    notes_title += " " + release.name + " " + release.date;
    var confluence_ancestors_id = "20648750";
    var confluence_space_key = "DAEO";

    page_content = '<p>' + release.body + '</p>' + page_content;

    var data = {
                "type": "page",
                "title": notes_title,
                "ancestors": [{
                    "id": confluence_ancestors_id
                }],
                "space": {
                    "key": confluence_space_key
                },
                "body": {
                    "storage": {
                        "value": page_content,
                        "representation": "storage"
                    }
                }
            };
    var post_data = JSON.stringify(data);

    var auth = new Buffer(login).toString('base64');

    var post_options = {
        host: '<confluence endpoint>', 
        port: 443,
        path: '/wiki/rest/api/content?expand=body.storage',
        method: 'POST',
        headers: {
            'Content-Length': Buffer.byteLength(post_data),
            'Content-Type': 'application/json',
            'Authorization': 'Basic ' + auth
        }
    };

    var post_request = https.request(post_options, function(response) {
        var body = '';
        console.log('STATUS: ' + response.statusCode);
        console.log('HEADERS: ' + JSON.stringify(response.headers));

        response.on('data', function(chunk)  {
            body += chunk;
        });

        response.on('end', function() {
            console.log("Response End");
        });

        response.on('error', function(e) {
            console.log('error:' + e.message);
        });
    });
    post_request.write(post_data);
    post_request.end();
}
var create_notes = function (gitToken, wikiUser, wikiPass) {
    const client = github.client(gitToken);
    var brand_repo = 'kumargan/microlending-platform';  //git repo
    const ghrepo = client.repo(brand_repo);
    getLatestCommits(ghrepo, function (collection, tags) {
        var promiseArray = [];

        console.log(`Found ${collection.length} commits`);
        for(var i = 0; i < collection.length; i++) {
            promiseArray.push(getCommit(ghrepo, collection[i]));
        };

        Promise.all(promiseArray).then(values => {
            var release_content = '';
            values.map((value) =>
                {
                    var prefix = 'Merge';
                    if (!value.startsWith(prefix)) {
                        release_content += '<li>' + value + '<\/li>';
                    }
                }
            );
            if (release_content != '') {
                release_content = '<p><ul>' + release_content + '</ul></p>';
                release_content += '<p><strong><a href="https://github.com/'+brand_repo+'/releases/tag/'+tags[0].name;
                release_content += '" class="external-link" rel="nofollow">https://github.com/'+brand_repo+'/releases/tag/';
                release_content += tags[0].name+ '</a></strong></p>';

            } else {
                release_content = 'No changes';
            }

            getReleaseDetails(ghrepo, function(err, release_name, release_body, release_date) {
              var release = {name: release_name, body: release_body, date: release_date};
              credentials = wikiUser + ':' + wikiPass
              createConfluence(release_content, release, tags, credentials);
            })
        }).catch((err) => console.log('ERR: ', err));

    });
};

//create_notes();

module.exports.create_notes = create_notes;
