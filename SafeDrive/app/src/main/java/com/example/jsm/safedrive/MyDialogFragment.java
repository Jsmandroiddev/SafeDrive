package com.example.jsm.safedrive;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.jsm.safedrive.adapters.ShareLocDialogBoxAdapter;
import com.example.jsm.safedrive.adapters.ShareLocDialogBoxAdapter.getselectedUser;
import com.example.jsm.safedrive.bean.ShareLocDialoBoxBean;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;
import static com.example.jsm.safedrive.ShareLocationActivity.sharedAddress;

/**
 * Created by JSM on 1/10/2017.
 */

public class MyDialogFragment extends DialogFragment implements getselectedUser {

    RecyclerView recyclerView;
    CheckBox shareLocChkBox;
    RelativeLayout shareAddressbtn, btnDialogAddContacts;
    onSubmitListener mListener;
    View mvVew;
    ShareLocDialogBoxAdapter shareLocDialogBoxAdapter;


    ArrayList<ShareLocDialoBoxBean> dialoBoxBeenlist;
    ArrayList<ShareLocDialoBoxBean> selectedbeanList;

    ShareLocDialoBoxBean finalshareLocDialoBoxBean;
    Uri uriContact;
    private String contactID;

    public interface onSubmitListener {
        void checkBoxStatus(boolean status);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        setStyle(DialogFragment.STYLE_NORMAL, R.style.You_Dialog);

        dialoBoxBeenlist = new ArrayList<>();

        if (activity instanceof onSubmitListener) {
            mListener = (onSubmitListener) activity;
        } else {
            throw new ClassCastException();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ArrayList<ShareLocDialoBoxBean> shareLocDialolist = new ArrayList<>();

        //final String newAddress = (String) getArguments().get("finalAddrs");

        selectedbeanList = new ArrayList<>();

        if (mvVew == null) {
            mvVew = inflater.inflate(R.layout.sharelocation_custom_dialog, container, false);
        } else {
            ((ViewGroup) mvVew.getParent()).removeView(mvVew);
        }

        recyclerView = (RecyclerView) mvVew.findViewById(R.id.shareloc_rv);
        shareLocChkBox = (CheckBox) mvVew.findViewById(R.id.shareloc_dialog_checkbox);
        shareAddressbtn = (RelativeLayout) mvVew.findViewById(R.id.btn_dialog_share_loc);
        btnDialogAddContacts = (RelativeLayout) mvVew.findViewById(R.id.btn_dialog_shareloc_add_more_contacts);


        btnDialogAddContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, 101);
            }
        });

        shareLocChkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (shareLocChkBox.isChecked()) {
                    for (int i = 0; i < dialoBoxBeenlist.size(); i++) {
                        dialoBoxBeenlist.get(i).setChecked(true);
                    }
                    shareLocDialogBoxAdapter = new ShareLocDialogBoxAdapter(dialoBoxBeenlist, MyDialogFragment.this);

                    recyclerView.setHasFixedSize(true);

                    recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL));
                    recyclerView.setAdapter(shareLocDialogBoxAdapter);

                    shareLocDialogBoxAdapter.notifyDataSetChanged();
                } else {
                    for (int i = 0; i < dialoBoxBeenlist.size(); i++) {
                        dialoBoxBeenlist.get(i).setChecked(false);
                    }
                    shareLocDialogBoxAdapter = new ShareLocDialogBoxAdapter(dialoBoxBeenlist, MyDialogFragment.this);

                    recyclerView.setHasFixedSize(true);

                    recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL));
                    recyclerView.setAdapter(shareLocDialogBoxAdapter);

                    shareLocDialogBoxAdapter.notifyDataSetChanged();
                }
            }
        });

        shareAddressbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!sharedAddress.equals("off")) {
                    if (shareLocChkBox.isChecked()) {

                        if (!sharedAddress.equalsIgnoreCase("Can't get Address!")) {
                            Toast.makeText(getActivity(), "Your Location " + sharedAddress + " is send to", Toast.LENGTH_LONG).show();
                            for (int i = 0; i < dialoBoxBeenlist.size(); i++) {
                                Toast.makeText(getActivity(), dialoBoxBeenlist.get(i).getContactName(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getActivity(), "Can't get Address!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        if (selectedbeanList != null && !selectedbeanList.isEmpty()) {

                            if (!sharedAddress.equalsIgnoreCase("Can't get Address!")) {
                                Toast.makeText(getActivity(), "Your Location " + sharedAddress + " is send to", Toast.LENGTH_LONG).show();
                                for (int i = 0; i < selectedbeanList.size(); i++) {
                                    Toast.makeText(getActivity(), selectedbeanList.get(i).getContactName(), Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getActivity(), "Can't get Address!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }else
                    Toast.makeText(getActivity(), "GPS Location is Off,please On GPS Location to get address", Toast.LENGTH_SHORT).show();
            }
        });

        return mvVew;
    }

    @Override
    public void getUser(final ArrayList<ShareLocDialoBoxBean> locDialoBoxBeen) {

        if (locDialoBoxBeen != null) {
            selectedbeanList = locDialoBoxBeen;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 101:
                if (requestCode == 101) {
                    if (data != null) {
                        uriContact = data.getData();

                        String name = retrieveContactName();
                        String number = retrieveContactNumber();
                        Bitmap pic = retrieveContactPhoto();


                        if (name != null && number != null && pic != null) {
                            finalshareLocDialoBoxBean = new ShareLocDialoBoxBean(name, number, false, pic);
                        } else {
                            if (name == null && pic == null && number != null) {
                                finalshareLocDialoBoxBean = new ShareLocDialoBoxBean("No name", number, false, BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.user_image));
                            } else {
                                if (name != null && number != null && pic == null) {
                                    finalshareLocDialoBoxBean = new ShareLocDialoBoxBean(name, number, false, BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.user_image));
                                } else {
                                    if (name == null && number != null && pic != null) {
                                        finalshareLocDialoBoxBean = new ShareLocDialoBoxBean("No name", number, false, pic);
                                    } else {
                                        Toast.makeText(getActivity(), "No number available for this contact", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            }
                        }
                        if (finalshareLocDialoBoxBean != null) {

                            dialoBoxBeenlist.add(finalshareLocDialoBoxBean);

                            shareLocDialogBoxAdapter = new ShareLocDialogBoxAdapter(dialoBoxBeenlist, MyDialogFragment.this);
                            recyclerView.setHasFixedSize(true);
                            recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL));
                            recyclerView.setAdapter(shareLocDialogBoxAdapter);
                            shareLocDialogBoxAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getActivity(), "no contact selected", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(getActivity(), "no contact selected", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }

            default:
                Toast.makeText(getActivity(), "no contact selected", Toast.LENGTH_SHORT).show();
        }
    }

    private String retrieveContactNumber() {

        String contactNumber = null;

        // getting contacts ID
        Cursor cursorID = getActivity().getContentResolver().query(uriContact,
                new String[]{ContactsContract.Contacts._ID},
                null, null, null);

        if (cursorID.moveToFirst()) {

            contactID = cursorID.getString(cursorID.getColumnIndex(ContactsContract.Contacts._ID));
        }

        cursorID.close();

        Log.d(TAG, "Contact ID: " + contactID);

        // Using the contact ID now we will get contact phone number
        Cursor cursorPhone = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},

                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? AND " +
                        ContactsContract.CommonDataKinds.Phone.TYPE + " = " +
                        ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE,

                new String[]{contactID},
                null);

        if (cursorPhone.moveToFirst()) {
            contactNumber = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

        }

        cursorPhone.close();

        Log.d(TAG, "Contact Phone Number: " + contactNumber);

        return contactNumber;
    }

    private String retrieveContactName() {

        String contactName = null;

        // querying contact data store
        Cursor cursor = getActivity().getContentResolver().query(uriContact, null, null, null, null);

        if (cursor.moveToFirst()) {

            // DISPLAY_NAME = The display name for the contact.
            // HAS_PHONE_NUMBER =   An indicator of whether this contact has at least one phone number.

            contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
        }

        cursor.close();

        Log.d(TAG, "Contact Name: " + contactName);

        return contactName;

    }

    private Bitmap retrieveContactPhoto() {

        Bitmap photo = null;

        try {
            InputStream inputStream = ContactsContract.Contacts.openContactPhotoInputStream(getActivity().getContentResolver(),
                    ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, new Long(contactID)));

            if (inputStream != null) {
                photo = BitmapFactory.decodeStream(inputStream);

                inputStream.close();
            }

            assert inputStream != null;


        } catch (IOException e) {
            e.printStackTrace();
        }

        return photo;
    }

}
