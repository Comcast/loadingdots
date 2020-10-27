# Loading Dots Library
Library and Sample app for a VectorDrawable-based loading dots view

## Usage

Library includes a LoadingDots class which inherits from and can be used analogously to an ImageView.  That means it can be inclued in XML or inflated programmatically.

example:

```
<com.xfinity.loadingdots.LoadingDots
               android:id="@+id/loading_dots"
               android:layout_width="wrap_content"
               android:layout_height="wrap_content"
               android:layout_gravity="center"/>
```

The LoadingDots View will scale to any width/height dimensions without any pixelization or any other traditional ImageView artifacts.

In addition, since it uses an AnimatedVectorDrawable, animations excute on the Render Thread, which preserves smoothness even in the face of intense, simultaneous UI Thread usage. Animations will start and end automatically based on the visibility of the LoadingDots view, so at this time you must directly change the visibility of the view. If you have the view in a parent whose visibility you change, the dots will continue animating even though they cannot be seen. 

Currently, the animations are not customizable.  However, the colors of the dots can be altered by overriding the resources in the client application.  To do so, in your colors.xml file, simply include
 
 ```xml 
 <color name="dot_start">your-starting-color</color>
 <color name="dot_end">your-end-color</color>
 ```
 
Each dot will animate from the start to the end color as it runs through its scaling animation sequence, with the starting color at the unscaled point.

The full implementation is included in the sample app

LoadingDots are backwards compatible to API 11
  
## Dependency

Include the library in your project by adding

```xml
implementation 'com.xfinity:loadingdots:1.0@aar'
```

To your build.gradle
