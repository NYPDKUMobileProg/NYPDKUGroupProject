package com.example.cashie.cashie;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.cashie.cashie.MenFragment.OnListFragmentInteractionListener;
import com.example.cashie.cashie.Controller.ItemController.Item;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.DecimalFormat;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Item} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */

public class MyMenRecyclerViewAdapter extends RecyclerView.Adapter<MyMenRecyclerViewAdapter.ViewHolder> {

    private final List<Item> mValues;
    private final OnListFragmentInteractionListener mListener;
    //private Context context;

    public MyMenRecyclerViewAdapter(List<Item> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_men, parent, false);
        //context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        DecimalFormat formatter = new DecimalFormat("$##.00");
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).id);
        holder.mNameView.setText(mValues.get(position).name);
        holder.mSizeView.setText(mValues.get(position).size);
        holder.mMakerView.setText(mValues.get(position).maker);
        holder.mPriceView.setText(formatter.format(mValues.get(position).price));

        StorageReference imageRef = FirebaseStorage.getInstance().getReference().child("men/" + mValues.get(position).id + ".jpg");
        Glide.with(holder.mPhotoView.getContext())
                .using(new FirebaseImageLoader())
                .load(imageRef)
                .into(holder.mPhotoView);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    // Clicked outside of button
                    Context context = v.getContext();
                    Intent intent = new Intent(context, ItemDetailActivity.class);
                    intent.putExtra(ItemDetailFragment.ARG_ITEM_ID, holder.mItem.id);

                    context.startActivity(intent);
                }
            }
        });

        holder.mButtonCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Click button
            if (null != mListener) {
                mListener.onListFragmentInteraction(holder.mItem);
            }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView mPhotoView;
        public final TextView mIdView;
        public final TextView mNameView;
        public final TextView mSizeView;
        public final TextView mMakerView;
        public final TextView mPriceView;
        public final Button mButtonCart;
        public Item mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mPhotoView = (ImageView) view.findViewById(R.id.item_image);
            mIdView = (TextView) view.findViewById(R.id.id);
            mNameView = (TextView) view.findViewById(R.id.item_category);
            mSizeView = (TextView) view.findViewById(R.id.item_size);
            mMakerView = (TextView) view.findViewById(R.id.item_seller);
            mPriceView = (TextView) view.findViewById(R.id.item_price);
            mButtonCart = (Button) view.findViewById(R.id.add_to_cart);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mNameView.getText() + "'";
        }
    }
}