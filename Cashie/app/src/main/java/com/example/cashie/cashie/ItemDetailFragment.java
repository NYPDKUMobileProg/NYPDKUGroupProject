package com.example.cashie.cashie;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.cashie.cashie.Controller.ItemController;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.DecimalFormat;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link ItemListActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 */
public class ItemDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private ItemController.Item mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = ItemController.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));

            /*Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.name);
            }*/
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.item_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            DecimalFormat formatter = new DecimalFormat("$##.00");
            //((TextView) rootView.findViewById(R.id.item_detail)).setText(mItem.maker);
            StorageReference imageRef = FirebaseStorage.getInstance().getReference().child(mItem.tag.toLowerCase() + "/" + mItem.id + ".jpg");
            Glide.with(rootView.getContext())
                    .using(new FirebaseImageLoader())
                    .load(imageRef)
                    .into((ImageView) rootView.findViewById(R.id.item_detail_image));
            ((TextView) rootView.findViewById(R.id.item_detail_piece_field)).setText(mItem.name);
            ((TextView) rootView.findViewById(R.id.item_detail_color_field)).setText(mItem.color);
            ((TextView) rootView.findViewById(R.id.item_detail_cut_field)).setText(mItem.cut);
            ((TextView) rootView.findViewById(R.id.item_detail_fabric_field)).setText(mItem.fabric);
            ((TextView) rootView.findViewById(R.id.item_detail_maker_field)).setText(mItem.maker);
            ((TextView) rootView.findViewById(R.id.item_detail_price_field)).setText(formatter.format(mItem.price));
            ((TextView) rootView.findViewById(R.id.item_detail_size_field)).setText(mItem.size);
            ((TextView) rootView.findViewById(R.id.item_detail_seller_field)).setText(mItem.seller);
        }

        return rootView;
    }
}
