package com.example.foodrecipeasync

class DataRecipe {
    var recipename: String = ""
    var recipeimage: String = ""
    var recipeid: Int = 0
    constructor() {}

    constructor(recipename: String,recipeimage:String,recipeId:Int) {
        this.recipename = recipename
        this.recipeimage = recipeimage
        this.recipeid = recipeid
    }

}