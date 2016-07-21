'use strict';

var moment = require('moment');

module.exports = [
  {
    "model": "Resource",
    "data": {
      "resourceId": "ed687098-7aeb-4b83-a931-1318d9141e2f",
      "resourcePath": "test-profile.png",
      "resourceType": "image"
    }
  },
  {
    "model": "User",
    "data": {
      "userId": "df44da10-7a27-41f6-abe8-5908e8c4d56a",
      "email": "brand1@test.com",
      "contactNumber": "012333444",
      "password": "hackme",
      "bank": {
        "bankId": "002"
      },
      "profilePicture": {
        "resourceId": "ed687098-7aeb-4b83-a931-1318d9141e2f"
      }
    }
  },
  {
    "model": "Brand",
    "data": {
      "brandId": "86d9ebb5-78e2-4c8c-8eb6-f0e61010e2d6",
      "brandName": "The brand 1",
      "user": {
        "userId": "df44da10-7a27-41f6-abe8-5908e8c4d56a"
      }
    }
  },
  {
    "model": "Campaign",
    "data": {
      "campaignId": "865b7f55-0316-47b0-9704-bc24eaba1dc5",
      "title": "Help spread reachrabbit like a cancer!",
      "description": "I need your help to spread virus Reachrabbit to the world. It should be 1920x1600 advertisement with 8 ugly rabbits and 2 bananas.",
      "proposalDeadline": moment().add(1, 'hours').format(),
      "submissionDeadline": moment().add(2, 'hours').format(),
      "status": "production",
      "category": {
        "categoryName": "Lifestyle"
      },
      "createdBy": "user1@test.com",
      "updatedBy": "user1@test.com",
      "brand" : {
        "brandId": "86d9ebb5-78e2-4c8c-8eb6-f0e61010e2d6"
      }
    }
  },
  {
    "model": "Campaign",
    "data": {
      "campaignId": "2d50a293-aa82-4cff-bb8d-bdf826d7ca15",
      "title": "STOP POKEMON GO!",
      "description": "STOP IT PLZ",
      "proposalDeadline": moment().add(1, 'hours').format(),
      "submissionDeadline": moment().add(2, 'hours').format(),
      "status": "open",
      "category": {
        "categoryName": "Food"
      },
      "createdBy": "user1@test.com",
      "updatedBy": "user1@test.com",
      "brand" : {
        "brandId": "86d9ebb5-78e2-4c8c-8eb6-f0e61010e2d6"
      }
    }
  },
  {
    "model": "Campaign",
    "data": {
      "campaignId": "e4e0283d-4cca-4909-8c47-d0d21808c101",
      "title": "START DIGIMON GO!",
      "description": "START IT PLZ",
      "proposalDeadline": moment().add(1, 'hours').format(),
      "submissionDeadline": moment().add(2, 'hours').format(),
      "status": "open",
      "category": {
        "categoryName": "Food"
      },
      "createdBy": "user1@test.com",
      "updatedBy": "user1@test.com",
      "brand" : {
        "brandId": "86d9ebb5-78e2-4c8c-8eb6-f0e61010e2d6"
      }
    }
  }
];
