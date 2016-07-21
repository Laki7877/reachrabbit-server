/**
 * Handle global API routing
 *
 * @author     Poon Wu <poon.wuthi@gmail.com>
 * @author     Pat Sabpisal <ecegrid@gmail.com>
 * @since      0.0.1
 */
'use strict';

var express   = require('express'),
    expressValidate  = require('express-jsonschema').validate,
    auth      = require('./middlewares/auth'),
    paginate  = require('./middlewares/pagination'),
    $         = require('./controllers'),
    S         = require('./schemas');

var multer = require('multer');
var upload = multer({ dest: '.tmp' });

// validators function
function validate(body, query) {
  return expressValidate({body: body, query: query});
}
function validatePost(body) {
  return validate(body);
}
function validateQuery(query) {
  return validate(undefined, query);
}


module.exports = function() {
  // initiate express router object
  var router = express.Router({
    caseSensitive: true
  });

  // test-only
  if(process.env.NODE_ENV === 'test') {
    router.post('/tests/influencerLogin', $.testController.influencerBypassLogin);
  }

  /*********************************
   * Authentication
   *********************************/
  router.post('/auth/facebook', $.authController.facebook);
  router.post('/auth/google', $.authController.google);
  router.post('/auth/instagram', $.authController.instagram);

  router.post('/login', $.authController.brandLogin);
  router.post('/login/admin', $.authController.adminLogin);

  /********************************
   * User
   ********************************/
  router.post('/users/influencer', $.userController.createInfluencer);
  router.get('/users/influencer', $.userController.listInfluencer);
  router.get('/users/influencer/:userId', $.userController.getInfluencer);

  router.post('/users/brand', $.userController.createBrand);
  router.get('/users/brand', $.userController.listBrand);
  router.get('/users/brand/:userId', $.userController.getBrand);

  router.get('/profiles', auth(), $.userController.getProfile);
  router.put('/profiles', auth(), $.userController.updateProfile);

  /*********************************
   * Campaign
   *********************************/
  router.get('/mycampaigns', auth(), paginate(), $.campaignController.listCampaignByRole); //done
  router.get('/campaigns', auth(), paginate(), $.campaignController.listCampaign); //done
  router.get('/campaigns/:campaignId', auth(), $.campaignController.getCampaign); //done
  router.post('/campaigns', auth('brand'), $.campaignController.createCampaign); //done
  router.put('/campaigns/:campaignId', auth('brand'), $.campaignController.updateCampaign); //done

  // create proposal
  router.post('/campaigns/:campaignId/proposals', auth('influencer'), $.campaignController.createProposal);  //done
  router.post('/campaigns/:campaignId/submissions', auth('influencer'),$.campaignController.createSubmission); //done
  router.get('/campaigns/:campaignId/proposals', auth('brand', 'influencer'), paginate(), $.campaignController.listProposal); //done
  router.get('/campaigns/:campaignId/submissions', auth('brand', 'influencer'), paginate(), $.campaignController.listSubmission); //done
  router.put('/campaigns/:campaignId/proposals/:proposalId', auth('brand'), $.campaignController.updateProposal); //done
  router.put('/campaigns/:campaignId/submissions/:submissionId', auth('brand'), $.campaignController.updateSubmission); //done

  router.post('/campaigns/:campaignId/transactions/confirm', auth('admin'), $.campaignController.confirmCampaignPayment); //done
  router.post('/campaigns/:campaignId/transactions', auth('brand'), $.campaignController.payForCampaign); //done

  router.get('/campaigns/:campaignId/transactions', auth('brand'), paginate(), $.campaignController.listTransaction); //done

  /*********************************
   * Submission
   *********************************/
  router.get('/submissions', auth('admin'), $.submissionController.listSubmission);
  router.get('/submissions/:submissionId', auth('admin'), $.submissionController.getSubmission);
  router.post('/submissions/:submissionId/proofs', auth('influencer'), $.submissionController.createSubmissionProof);
  router.put('/submissions/:submissionId/proofs', auth('admin'), $.submissionController.confirmSubmissionProof);

  /*********************************
   * Payment
   *********************************/
  router.get('/transactions', auth(), paginate(), $.paymentController.listPayment); //done
  router.put('/transactions', auth('admin'), $.paymentController.bulkUpdatePayment); //done
  router.get('/transactions/:transactionId', auth(), $.paymentController.getPayment); //done

  /*********************************
   * Data
   *********************************/
  router.get('/data/categories', $.dataController.getActiveCategories);
  router.get('/data/medium', $.dataController.getActiveMedium);
  router.get('/data/banks', $.dataController.getBanks);

  /*********************************
   * File
   *********************************/

  router.get ('/file', $.fileController.listAll);
  router.post('/file', upload.single('file'), $.fileController.uploadSingle);
  router.post('/file/remote', $.fileController.fromRemote);


  router.get('/default/create', $.defaultController.testCreate);
  router.get('/default/delete', $.defaultController.testDelete);
  router.get('/default/findAll', $.defaultController.testFind);
  router.get('/default/update', $.defaultController.testUpdate);

  return router;
};
