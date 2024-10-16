## Functions

### Composition

Main functions required for composition creation and rendering.

#### Functions:

- `composition(width=1280, height=720, frameRate=30.0)`  
Creates a composition.
Initializes `Width`, `Height`, `HalfWidth` and `HalfHeight` variables.

- `render(nodes=[])`  
Adds nodes to the scene.


### Nodes

Nodes creation functions.

#### Functions:

`arc(properties={}) : ArcNode`

`circle(properties={}) : CircleNode`

`ellipse(properties={}) : EllipseNode`

`group(nodes=[]) : GroupNode`

`guideGrid(properties={stepX: 100, stepY: 100, width: 1920, height: 1080}) : GuideGridNode`  
debug grid for easier nodes snapping

`image(url="", properties={}) : ImageNode`  
  **url**: either network image url, or local file: `file:/C:/path/to/image.png`

`line(properties={}) : LineNode`

`polygon(points=[], properties={}) : PolygonNode`

`polyline(points=[], properties={}) : PolylineNode`

`rectangle(properties={}) : RectangleNode`

`svgPath(properties={}) : SVGPathNode`

`text(properties={}) : TextNode`

`textFlow(nodes={}) : TextFlowNode`


### Node utils

Utility and animation functions for nodes.

#### Functions:

`bounds(node) : Map`  
Calculates bounds of the given node. Returns a Map with fields: `minX`, `minY`, `minZ`, `maxX`, `maxY`, `maxZ`, `centerX`, `centerY`, `centerZ`, `width`, `height`, `depth`

`font(properties={}) : Font`  
Creates a Font object.

`strokeDashArray(shapeNode, array=[])`  
Sets stroke dash values. Please, refer to the [Java FX documentation](https://openjfx.io/javadoc/21/javafx.graphics/javafx/scene/shape/Shape.html#getStrokeDashArray()).

`typewriter(textNode, text, frameStart, frameStep = 50 ms, reverse = false) : Number`  
Adds keyframes with typewriter animation to a `textNode` starting from `frameStart`. Each character of the `text` will be typed with `frameStep` frames delay.  
`reverse=false` type from empty string to `text`, `reverse=true`: type from `text` to empty string.  
Returns: end frame of the animation.


### Interpolators

Predefined interpolators.

#### Variables

- `linear : Interpolator`
- `hold : Interpolator`, `discrete : Interpolator`
- `easeIn : Interpolator`
- `easeOut : Interpolator`
- `ease : Interpolator`, `easeBoth : Interpolator`
