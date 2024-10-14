## Types

- `Boolean` — logical value.

  `false true 1 0`

- `Number` — integer or float-point number.

  `-2 0.001 3.14 800`

- `String` — text value.

  Can be multiline. Escaping also supported.

  `"Hello"`, `'text'`, `"\uf004"`, `'line 1\nline 2'`

  ```
  "multiline
  text
  example"
  ```

- `Enum` — enumeration.

  Can be specified as a name or a number value.

  `blendMode: "MULTIPLY"`, `blendMode: 3`

- `[]` — array.

  `[1, 2, 3]  ["a", "b", "c"]`

- `{}` — map (object).

  ```
  {
    key: "value",
    number: 10,
    array: [1, 2, 3,]
  }
  ```

- `Paint` — color.

  Specified as a string in one of the specified formats:`#RRGGBB`, `#RRGGBBAA`, `#RGB`, `#RGBA` or as a color name.

  `"#ff00ff"`, `"#F0F"`, `"#FF00FF7F"`, `"white"`

- `Font` — font.

  Can be specified as a number (font size only) or as an object.

  ```
  font: {
    family: "monospace",
    weight: 400,
    italic: false,
    size: 32
  }
  ```   

- `Node` — `ObjectNode` object.
