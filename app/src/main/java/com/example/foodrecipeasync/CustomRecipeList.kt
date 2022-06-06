package com.example.foodrecipeasync

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class CustomRecipeList(private val recipe:List<DataRecipe>, private val listener: MainActivity) : RecyclerView.Adapter<CustomRecipeList.MyViewHolder>() {
    lateinit var titlename:String
    inner class  MyViewHolder(val view: View) : RecyclerView.ViewHolder(view),View.OnClickListener
    {
        fun bind(recipe: DataRecipe){
            var title: TextView = view.findViewById(R.id.textviewRecipe)
            var recipeimages: ImageView =view.findViewById(R.id.imageView)
            Glide.with(view.context).load(recipe.recipeimage).centerCrop().into( recipeimages)
            title.text= recipe.recipename
            titlename= recipe.recipename
        }
        init {
            view.setOnClickListener(this)
        }
        override fun onClick(p0: View?) {
            val position = adapterPosition
            if(position!=RecyclerView.NO_POSITION){
                listener.onItemClick(position)
            }
        }
    }    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_items, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(recipe[position])
    }
    override fun getItemCount(): Int {
        return recipe.size
    }
    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }
}