'use strict';

var moment = require('moment');

module.exports = [
  {
    "model": "Resource",
    "data": {
      "resourceId": "07ec9a84-f137-4a7e-b27a-240e1701b6c4",
      "resourcePath": "test-profile.png",
      "resourceType": "image"
    }
  },
  {
    "model": "User",
    "data": {
      "userId": "fd75cd48-f22c-49a1-9f73-2cfc246dcee3",
      "contactNumber": "012333444",
      "Bank": {
        "bankId": "002"
      },
      "profilePicture": "07ec9a84-f137-4a7e-b27a-240e1701b6c4"
    }
  },
  {
    "model": "Influencer",
    "data": {
      "influencerId": "cb8cf696-ea59-4884-909e-0185ad36ca05",
      "gender": "male",
      "about": "This is about me",
      "Media": [
        {
          "mediaName": "facebook",
          "_through": {
            "socialId": "128000600963755"
          }
        },
        {
          "mediaName": "youtube",
          "_through": {
            "socialId": "mytube"
          }
        }
      ],
      "User": {
        "userId": "fd75cd48-f22c-49a1-9f73-2cfc246dcee3"
      }
    }
  },
  {
    "model": "CampaignProposal",
    "data": {
      "proposalId": "ab8061ce-18cd-4a5d-9eea-b4563cffab47",
      "title": "Proposal #1",
      "description": "This is my proposal",
      "status": "propose",
      "Campaign": {
        "campaignId": "865b7f55-0316-47b0-9704-bc24eaba1dc5"
      },
      "Influencer": {
        "influencerId": "cb8cf696-ea59-4884-909e-0185ad36ca05"
      }
    }
  },
  {
    "model": "CampaignProposal",
    "data": {
      "proposalId": "bcd7bc2a-61d3-431d-a0e5-2d681ca63db5",
      "title": "Proposal #2",
      "description": "This is my proposal",
      "status": "propose",
      "Campaign": {
        "campaignId": "2d50a293-aa82-4cff-bb8d-bdf826d7ca15"
      },
      "Influencer": {
        "influencerId": "cb8cf696-ea59-4884-909e-0185ad36ca05"
      }
    }
  },
  {
    "model": "CampaignSubmission",
    "data": {
      "submissionId": "695194c4-05a0-4adc-9643-9d653d59bca3",
      "title": "Submission #1",
      "description": "This is my submission",
      "Campaign": {
        "campaignId": "2d50a293-aa82-4cff-bb8d-bdf826d7ca15"
      },
      "Influencer": {
        "influencerId": "cb8cf696-ea59-4884-909e-0185ad36ca05"
      }
    }
  }
];
