package com.ua.bellkross.android.carsharingclient.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.bellkross.carsharingserver.entity.Car
import com.ua.bellkross.android.carsharingclient.R
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject

class CarsAdapter : RecyclerView.Adapter<CarsAdapter.ViewHolder>() {

    val cars: MutableList<Car> = ArrayList()
    val itemClick: Subject<Int> = PublishSubject.create()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_item_car, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = cars.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvBrand.text = cars[position].brand
        holder.tvAddress.text = cars[position].address
        holder.tvPrice.text = cars[position].price.toString()
        holder.itemView.setOnClickListener {
            itemClick.onNext(position)
        }
    }

    //Check, will it work without initialization and just with using holder.itemView.tvBrand?
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvBrand: TextView = view.findViewById(R.id.tvBrand)
        val tvAddress: TextView = view.findViewById(R.id.tvAddress)
        val tvPrice: TextView = view.findViewById(R.id.tvPrice)
    }
}