## Supported Commands (ImageProcessingImplPro):

Commands are read line by line. Only the first word in the line is checked as a command, with
the rest of the words in the line being treated as the arguments to the command.

- `load image-path image-name` (Loads an image from the specified path and refers to it henceforth
  in the program by the given name)
    - Can be run at any point as long as the image-path contains a valid image
    - example: `load res/img1.ppm img`
    - example: `load res/img1.png random-word`


- `save image-path image-name` (Saves the image with the given name to the specified path which
  includes the name of the file)
    - Must have already loaded or created an image with the given name
    - example: `save res/img.jpg img`
    - example: `save res/testing.bmp random-word`


- `(component name)-component image-name dest-image-name` (Create a greyscale image with the
  (component name) component of the image with the given name.
  [supported (component name): red, green, blue, value, luma, intensity])
    - Must have already loaded or created an image with the given name
    - example: `red-component red-img img`
    - example: `green-component green-img img`
    - example: `blue-component blue-img img`
    - example: `value-component value-img img`
    - example: `luma-component luma-img img`
    - example: `intensity-component intensity-img img`


- `horizontal-flip image-name dest-image-name` (Flip an image horizontally to create a new image,
  referred to henceforth by the given destination name)
    - Must have already loaded or created an image with the given name
    - example: `horizontal-flip img mirror-img`

- `vertical-flip image-name dest-image-name` (Flip an image vertically to create a new image,
  referred to henceforth by the given destination name)
    - Must have already loaded or created an image with the given name
    - example: `vertical-flip img upsidown-img`


- `brighten increment image-name dest-image-name` (Brighten the image by the given increment to
  create a new image, referred to henceforth by the given destination name - the increment may be
  positive (brightening) or negative (darkening))
    - Must have already loaded or created an image with the given name
    - example: `brighten 50 img brighter`
    - example: `brighten -70 img darker`


- `blur image-name dest-image-name` (Blue an image to create a new image, referred to henceforth by
  the given destination name)
    - Must have already loaded or created an image with the given name
    - example: `blur img blurry`


- `sharpen image-name dest-image-name` (Sharpen an image to create a new image, referred to
  henceforth by the given destination name)
    - Must have already loaded or created an image with the given name
    - example: `shapen blurry img`


- `greyscale image-name dest-image-name` (Find the greyscale version an image to create a new image,
  referred to henceforth by the given destination name)
    - Must have already loaded or created an image with the given name
    - example: `greyscale img grey-img`


- `sepia image-name dest-image-name` (Find the sepia version of an image to create a new image,
  referred to henceforth by the given destination name)
    - Must have already loaded or created an image with the given name
    - example: `sepia img yellowed-img`


- `menu` (Print supported instruction list)
    - Can be run at any point
    - example: `menu`


- `q` or `quit` (quit the program)
    - Can be run at any point
    - example: `q`
    - example: `quit`

