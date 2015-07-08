package com.andela.uberhack;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.andela.uberhack.models.User;
import com.google.gson.Gson;
import com.androidsocialnetworks.lib.AccessToken;
import com.androidsocialnetworks.lib.SocialNetwork;
import com.androidsocialnetworks.lib.SocialNetworkManager;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import com.androidsocialnetworks.lib.SocialPerson;
import com.androidsocialnetworks.lib.listener.OnLoginCompleteListener;
import com.androidsocialnetworks.lib.listener.OnRequestSocialPersonCompleteListener;


import org.json.JSONException;
import org.json.JSONObject;


public abstract class AuthActivityFragment extends Fragment {

    public AuthActivityFragment() {

    }

    protected SocialNetworkManager mSocialNetworkManager;
    protected boolean mSocialNetworkManagerInitialized = false;
    public static final String SOCIAL_NETWORK_TAG = "LoginFragment.SOCIAL_NETWORK_TAG";

    protected ImageButton mTwitterButton;
    protected ImageButton mLinkedInButton;
    protected ImageButton mFacebookButton;
    protected ImageButton mGooglePlusButton;

    protected abstract void postLogin();

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        Vars.init(getActivity().getApplicationContext());
        mSocialNetworkManager = (SocialNetworkManager) getFragmentManager().findFragmentByTag(SOCIAL_NETWORK_TAG);

        if (mSocialNetworkManager == null) {
            mSocialNetworkManager = SocialNetworkManager.Builder.from(getActivity())
                    .twitter("wuOtjLCXpjIMiNLNkWuTWpAn9", "AEonxjtNRbMBpkoDF60aH2P6exGQKCipAPNT7wfdpeUXQMtkIh")
                    .linkedIn("770ucbdyc6idoa", "vnIZNqQIVIDzk8Dh", "r_basicprofile+rw_nus+r_network+w_messages+r_emailaddress+r_network")
                    .googlePlus()
                    .facebook()
                    .build();
            getFragmentManager().beginTransaction().add(mSocialNetworkManager, SOCIAL_NETWORK_TAG).commit();
        } else {
            mSocialNetworkManagerInitialized = true;
        }

        mSocialNetworkManager.setOnInitializationCompleteListener(new SocialNetworkManager.OnInitializationCompleteListener() {
            @Override
            public void onSocialNetworkManagerInitialized() {
                Init();
            }
        });
        setRetainInstance(true);
    }

    private void Init() {

        try {
            for (SocialNetwork socialNetwork : mSocialNetworkManager.getInitializedSocialNetworks()) {
                if (socialNetwork.isConnected()) {
                    getUser(socialNetwork.getID());
                    return;
                }
            }

            if (Vars.currentUser == null) {
                Vars.isCenterLoading(getActivity(), getView(), Vars.isLoginLoading);
            } else {
                getUser(0);
            }
        } catch (Exception e0) {
            e0.printStackTrace();
            Vars.Toaster(e0.toString() + "\n" + e0.getMessage() + "\n@LoginFragment.Init()", getActivity(), 0);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_auth, container, false);
    }

    private AccessToken AccessTokenAndSecret = null;


    private void redirect(User user, int i) {
        Vars.socialNetworkID = i;
        Vars.mSocialNetworkManager = mSocialNetworkManager;

        //Vars.saveUser(user);
        postLogin();

//        Log.i("name", Vars.currentUser.name);
//        Log.i("enabled", Vars.currentUser.enabled + "");
//        Log.i("id", Vars.currentUser._id + " Logged in");
    }

    //private String user_data;



    public void getUser(int i) {
        if (Vars.currentUser != null) {

            return;
        }

        Vars.isCenterLoading(getActivity(), getView(), true);

        OnRequestSocialPersonCompleteListener requestPerson = new OnRequestSocialPersonCompleteListener() {
            @Override
            public void onRequestSocialPersonSuccess(final int i, final SocialPerson socialPerson) {
                String token = "", secret = "";
                try {
                    token = AccessTokenAndSecret.token;
                    secret = AccessTokenAndSecret.secret;
                } catch (NullPointerException npe) {
                    npe.printStackTrace();
                }
                new Vars.MakeHTTPRequest(getActivity(), "users", Vars.userKeys,
                        new String[]{
                                token,
                                secret
                        }
                        , Vars.POST) {
                    @Override
                    protected void done(String result) {
                        try {
                            Log.i("user_json", result);
                            User user = new Gson().fromJson(result, User.class);
                            redirect(user, i);
                        } catch (Exception e0) {
                            e0.printStackTrace();
                        }
                    }
                };
            }

            @Override
            public void onError(int i, String s, String s2, Object o) {

            }
        };


        SocialNetwork socialNetwork = mSocialNetworkManager.getSocialNetwork(i);
        socialNetwork.requestCurrentPerson(requestPerson);
        AccessTokenAndSecret = socialNetwork.getAccessToken();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mGooglePlusButton = (ImageButton) view.findViewById(R.id.google_plus_button);


        mGooglePlusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SocialNetWorkLogin(3);
            }
        });



//        view.findViewById(R.id.loginButton).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
    }

    private void SocialNetWorkLogin(int socialNetworkId) {
        final OnLoginCompleteListener LoginComplete = new OnLoginCompleteListener() {
            @Override
            public void onLoginSuccess(int i) {

            }

            @Override
            public void onError(int i, String s, String s2, Object o) {
                Vars.isCenterLoading(getActivity(), getView(), false);
                Vars.isLoginLoading = false;
                Vars.Toaster("Error Logging in [" + i + "], from " + s + "\n\n" + s2, getActivity(), 0);
            }
        };

        SocialNetwork socialNetwork = mSocialNetworkManager.getSocialNetwork(socialNetworkId);
        if (!socialNetwork.isConnected()) {
            try {
                socialNetwork.requestLogin(LoginComplete);
                Vars.isCenterLoading(getActivity(), getView(), true);
                Vars.isLoginLoading = true;
            } catch (Exception e0) {
                Vars.isLoginLoading = false;
                e0.printStackTrace();
            }
        }
    }

    //    protected boolean checkIsLoginned(int socialNetworkID) {
//        if (mSocialNetworkManager.getSocialNetwork(socialNetworkID).isConnected()) {
//            return true;
//        }
//
//        AlertDialogFragment
//                .newInstance("Request Login", "This action request login, please go to login demo first.")
//                .show(getFragmentManager(), null);
//
//        return false;
//    }
//    public void onRequestCancel() {
//        for (SocialNetwork socialNetwork : mSocialNetworkManager.getInitializedSocialNetworks()) {
//            socialNetwork.cancelAll();
//        }
//    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }
}