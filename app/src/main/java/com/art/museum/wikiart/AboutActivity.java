package com.art.museum.wikiart;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spanned;
import android.widget.TextView;

/**
 * Created by arpita on 06/11/16.
 */
public class AboutActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_the_app);
        TextView about = (TextView) findViewById(R.id.about_app);
        //String description = this.getString(R.string.description);
        String description = "<div align=\"justify\"> ArtBrowse is an assemblage of 10000 paintings organized not by artist or time period but by similarity. The content is provided by Wikiart.org<br/>"+
        "Click on something you like, then look deeper to discover related works as seen through computer vision and combinatorial optimization. "+
                "This app is a demonstration of our DeepBrowse approach for efficiently scanning through large lists of items in the absence of a predefined hierarchy.<br/>"+
                "DeepBrowse is defined by the interaction of two fixed, globally-defined permutations on the space of objects: one ordering the items by similarity, the second based on magnitude or importance.<br/>"+
                "Constructing good similarity orders of large collections of complex objects is a challenging task.<br/>"+
                "We use graph embeddings to provide the features to order the paintings by similarity.<br/>"+
                "The problem of ordering items in a list by similarity is natural modeled by the Traveling Salesman Problem (TSP), which seeks the minimum-cost tour visiting the complete set of items.<br/>"+
                "Try our other DeepBrowse apps for Wikipedia, movies, and words.<br/>"+
                "ArtBrowse is a product of the Data Science Laboratory, Dept. of Computer Science, Stony Brook University.<br/>"+
                "Please visit http://sites.google.com/site/datascienceslab </p>";
        Spanned formatted = Html.fromHtml(description);
        about.setText(formatted);

    }
}
