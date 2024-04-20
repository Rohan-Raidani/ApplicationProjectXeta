package com.example.xetaanalytics.Activity.Adapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.xetaanalytics.R
import com.squareup.picasso.Picasso
import com.example.xetaanalytics.Activity.model.SimilarItem


// Define a mapping of item names to image URLs
val itemImageMap = mapOf(
    "Veg Biryani" to "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSQBBuXULpx2XGpSON72zsFYKhWT1uh2U033459CegKfw&s",
    "Paneer Biryani" to "https://www.chefkunalkapur.com/wp-content/uploads/2021/10/SRG_-CKK-Shoot-18_226-1300x867.jpg?v=1633580575",
    "Mutton Biryani" to "https://www.indianhealthyrecipes.com/wp-content/uploads/2019/02/mutton-biryani-recipe-500x500.jpg",
    // Add more mappings as needed
)

class SimilarRecyclerAdapter (val context : Context, private val  similarList:ArrayList<SimilarItem>) : RecyclerView.Adapter<SimilarRecyclerAdapter.SimilarViewHolder>(){
    class SimilarViewHolder(view: View): RecyclerView.ViewHolder(view){
        val itemName :TextView = view.findViewById(R.id.itemName)


        val llimg : LinearLayout = view.findViewById(R.id.llimg)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimilarViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.similar_item, parent, false)
        return SimilarViewHolder(view)
    }

    override fun onBindViewHolder(holder: SimilarViewHolder, position: Int) {
        val item = similarList[position]
        holder.itemName.text = item.name
        // Load image using Picasso and set it as background
        // Check if the image URL is empty or incorrect

            // If image URL is empty or incorrect, try to load the default image
            val defaultImageUrl = itemImageMap[item.name] // Get the default image URL from the itemImageMap
            if (defaultImageUrl != null) {
                // Load the default image from the online source
                Picasso.get().load(defaultImageUrl).into(object : com.squareup.picasso.Target {
                    override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                        holder.llimg.background = BitmapDrawable(context.resources, bitmap)
                    }

                    override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
                        // Handle error case if needed
                        // If loading the default image fails, you may want to set a fallback drawable
                        holder.llimg.setBackgroundResource(R.drawable.chick_top)
                    }

                    override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                        // You can add a placeholder if needed
                    }
                })
            }


        }


    override fun getItemCount(): Int {
       return similarList.size
    }
}