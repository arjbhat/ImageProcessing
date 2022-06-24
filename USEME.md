## GUI commands

- `load`
    - Press the load image button on the bottom right. Select an image, press enter, then
      optionally provide a name to call the image.
- `save`
    - Press the save image button on the bottom right. Choose the right folder and write the file
      name. Press enter to save the image.
- `brighten`
    - Select the `brighten` option from the right side dropdown. Drag the slider to the amount
      to change the brightness by. Press the apply button.
      You can optionally provide a new name to retain access to the previous image.
- `downscale`
    - Select the `downscale` option from the right side dropdown. enter the new dimensions of
      the image. Press the apply button.
      You can optionally provide a new name to retain access to the previous image.
- `red-component`
    - Select the `red-component` option from the right side dropdown. Press the apply button.
      You can optionally provide a new name to retain access to the previous image.
- `green-component`
    - Select the `green-component` option from the right side dropdown. Press the apply button.
      You can optionally provide a new name to retain access to the previous image.
- `blue-component`
    - Select the `blue-component` option from the right side dropdown. Press the apply button.
      You can optionally provide a new name to retain access to the previous image.
- `value-component`
    - Select the `value-component` option from the right side dropdown. Press the apply button.
      You can optionally provide a new name to retain access to the previous image.
- `luma-component`
    - Select the `luma-component` option from the right side dropdown. Press the apply button.
      You can optionally provide a new name to retain access to the previous image.
- `intensity-component`
    - Select the `intensity-component` option from the right side dropdown. Press the apply button.
      You can optionally provide a new name to retain access to the previous image.
- `horizontal-flip`
    - Select the `horizontal-flip` option from the right side dropdown. Press the apply button.
      You can optionally provide a new name to retain access to the previous image.
- `vertical-flip`
    - Select the `vertical-flip` option from the right side dropdown. Press the apply button.
      You can optionally provide a new name to retain access to the previous image.
- `blur`
    - Select the `blur` option from the right side dropdown. Press the apply button.
      You can optionally provide a new name to retain access to the previous image.
- `sharpen`
    - Select the `sharpen` option from the right side dropdown. Press the apply button.
      You can optionally provide a new name to retain access to the previous image.
- `greyscale`
    - Select the `greyscale` option from the right side dropdown. Press the apply button.
      You can optionally provide a new name to retain access to the previous image.
- `sepia`
    - Select the `sepia` option from the right side dropdown. Press the apply button.
      You can optionally provide a new name to retain access to the previous image.

## Supported Text Commands (ImageProcessingImplPro):

Commands are read line by line. Only the first word in the line is checked as a command, with
the rest of the words in the line being treated as the arguments to the command.
(note: size, downscale, & mask variants are available in the ProMax controller)

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

