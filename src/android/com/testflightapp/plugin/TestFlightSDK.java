/**
 * TestFlightSDK plugin is available under *either* the terms of the modified BSD license *or* the
 * MIT License (2008). See http://opensource.org/licenses/alphabetical for full text.
 *
 * Copyright (c) 2013, Alfonso Ferrandez
 */
package com.testflightapp.plugin;

import com.testflightapp.lib.TestFlight;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Application;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;

/**
 * This calls out to the TestFlight SDK and returns the result.
 *
 * @sa https://github.com/apache/cordova-android/blob/master/framework/src/org/apache/cordova/CordovaPlugin.java
 */
public class TestFlightSDK extends CordovaPlugin {
  public static final int TAKEOFF_CODE = 0x07400F;

  private static final String TAKEOFF = "takeOff";
  private static final String LOG = "remoteLog";
  private static final String SETOPTIONS = "setOptions";
  private static final String PASSCHKPT = "passCheckpoint";
  private static final String OPENFEEDBACK = "openFeedbackView";
  private static final String SUBMITFEEDBACK = "submitFeedback";
  private static final String SETDEVID = "setDeviceIdentifier";
  private static final String TEAMTOKEN = "teamToken";

  private static final String LOG_TAG = "TestFlightSDK";

  private CallbackContext callbackContext;

  /**
   * Constructor.
   */
  public TestFlightSDK() {
  }

  /**
   * Executes the request.
   * <p/>
   * This method is called from the WebView thread. To do a non-trivial amount of work, use:
   * cordova.getThreadPool().execute(runnable);
   * <p/>
   * To run on the UI thread, use:
   * cordova.getActivity().runOnUiThread(runnable);
   *
   * @param action          The action to execute.
   * @param args            The exec() arguments.
   * @param callbackContext The callback context used when calling back into JavaScript.
   * @return Whether the action was valid.
   * @sa https://github.com/apache/cordova-android/blob/master/framework/src/org/apache/cordova/CordovaPlugin.java
   */
  @Override
  public boolean execute(String action, JSONArray args, CallbackContext callbackContext) {
    this.callbackContext = callbackContext;

    Log.d(LOG_TAG, "Execute: " + action + " - " + args.toString());
    if (action.equals(TAKEOFF)) {
      try {
        String teamToken = args.getString(0);
        try {
          takeOff(teamToken);
          this.callbackContext.success(teamToken);
        } catch (Exception ex) {
          Log.d(LOG_TAG, "Error taking off.", ex);
          this.callbackContext.error(ex.getMessage());
        }
      } catch (JSONException ex) {
        Log.d(LOG_TAG, "Error parsing JSONString from " + args.toString(), ex);
        callbackContext.error("An invalid Team Token was provided!");
      }
      return true;
    } else if (action.equals(SETDEVID)) {
      try {
        String devId = args.getString(0);
        try {
          setDeviceId(devId);
          this.callbackContext.success(devId);
        } catch (Exception ex) {
          Log.d(LOG_TAG, "Error setting Device UUID.", ex);
          this.callbackContext.error(ex.getMessage());
        }
      } catch (JSONException ex) {
        Log.d(LOG_TAG, "Error parsing JSONString from " + args.toString(), ex);
        callbackContext.error("An invalid Device UUID was provided!");
      }
      return true;
    } else if (action.equals(PASSCHKPT)) {
      try {
        String checkPoint = args.getString(0);
        try {
          passCheckpoint(checkPoint);
          this.callbackContext.success(checkPoint);
        } catch (Exception ex) {
          Log.d(LOG_TAG, "Error passing checkpoint.", ex);
          this.callbackContext.error(ex.getMessage());
        }
      } catch (JSONException ex) {
        Log.d(LOG_TAG, "Error parsing JSONString from " + args.toString(), ex);
        callbackContext.error("An invalid checkpoint name was provided!");
      }
      return true;
    } else if (action.equals(LOG)) {
      try {
        String message = args.getString(0);
        try {
          remoteLog(message);
          this.callbackContext.success(message);
        } catch (Exception ex) {
          Log.d(LOG_TAG, "Error passing message.", ex);
          this.callbackContext.error(ex.getMessage());
        }
      } catch (JSONException ex) {
        Log.d(LOG_TAG, "Error parsing JSONString from " + args.toString(), ex);
        callbackContext.error("An invalid message was provided!");
      }
      return true;
    } else {
      return false;
    }
  }

  /**
   * Starts a TestFlight SDK intent to initialise the SDK with TakeOff
   */
  public void takeOff(String teamToken) {
    TestFlight.takeOff(((CordovaPlugin) this).cordova.getActivity().getApplication(), teamToken);
  }

  /**
   * Sets the device identifier
   */
  public void setDeviceId(String deviceId) {
    // Not supported by Android SDK 1.2 
    //TestFlight.setDeviceIdentifier(deviceId);
  }

  /**
   * Track when a user has passed a checkpoint after the flight has taken off. Eg. passed level 1, posted high score
   */
  public void passCheckpoint(String checkpointName) {
    TestFlight.passCheckpoint(checkpointName);
  }

  /**
   * Remote logging (synchronous)
   */
  public void remoteLog(String message) {
    TestFlight.log(message);
  }
}