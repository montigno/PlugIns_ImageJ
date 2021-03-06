[![](https://img.shields.io/badge/java-8-yellow.svg)](https://www.java.com/fr/download/)
[![](https://img.shields.io/badge/platform-Linux%2C%20OSX%2C%20Windows-orange.svg)](#)
[![](https://img.shields.io/badge/works%20with-ImageJ-1abc9c.svg)](https://imagej.nih.gov/ij/)


# How to install plugins?

- download the PlugIns [here](https://github.com/montigno/PlugIns_ImageJ/archive/refs/heads/main.zip)
- unzip it and copy the folders to the ImageJ/plugins folder.
- start ImageJ (or 'Help' -> 'Refresh menus' if ImageJ alread open).

# Atlas_Irmage

Atlas_Irmage plugin is an image segmentation viewer.
It is mainly used for the comparison of acquisition images with those of atlases.

<p align="left">
<img src="https://github.com/montigno/PlugIns_ImageJ/blob/main/Screenshot_Atlas.jpg" width="600">
</p>

# How to use Atlas_Irmage?

- first, open your images (otherwise a 'No image' message appears)
- go to the menu PlugIns/Atlas_Irmage/AtlasIrmage, a window appears.
- select the labels text in 'Labels Text'
- select the labels image (among the open images) in 'Labels Image'.
- click on 'start' button.
- a 'Synchronize Windows' box appears, click on 'Synchronize All'
- click on a region of Labels image.


# CEST_IRM

CEST_IRM allows to trace the CEST Z-spectrum and Asymmetry profiles.

<p align="left">
<img src="https://github.com/montigno/PlugIns_ImageJ/blob/main/Screenshot_CEST.jpg" width="600">
</p>

# How to use CEST_IRM?

 - first, open your image
 - go to the menu PlugIns/CEST_IRM/CEST_IRM
 - if the pluging doesn't find the frequency range (json file missing), a dialog window appears:
      - enter the frequency range : min max step (do not forget the white spaces)
      - click on ok
 - draw a ROI on your image (rectangle, oval, polygone or freehand), the profils appear on the graph
 - if you move or resize your ROI, the graph updates

# Release history

    21/09/2021 : version 1.1
	CEST_IRM : new dialog box to enter the frequency range

    30/03/2021 : version 1.0
	First repository

