package com.schantz.project.graph

import java.io.{FileWriter, File}
import scala.Array
import scala.xml.XML

/**
 * User: soren
 */
class GraphGenerator {
  var projectDefs = List[String]()
  var projectDependencies = Set[String]()

  def generate(startingDir: String, outFile: String) {
    val classpathFiles = getFiles(new File(startingDir)).filter(f => f.getAbsolutePath.endsWith(".classpath"))
    classpathFiles.foreach(cp => {
      val projectName = cp.getParentFile.getName
      projectDefs = makeProjectBox(projectName) :: projectDefs

      val xml = XML.loadFile(cp)
      val projects = xml \\ "classpathentry"
      projects.map(p => {
        val depProjectName = (p \\ "@path").text
        val kind = (p \\ "@kind").text
        val combineaccessrules = (p \\ "@combineaccessrules").text

        if(depProjectName.startsWith("/") && kind == "src" && combineaccessrules == "false") {
          val stripedprojectName = depProjectName.replace("/", "")
          projectDependencies = Set(stripedprojectName+"->"+projectName) ++ projectDependencies
          println(depProjectName+ " "+combineaccessrules)

        }
      })

    })
    val header = """digraph hierarchy {
                   | size="5,5"
                   | node[shape=record,style=filled,fillcolor=gray95]
                   | edge[dir=back, arrowtail=empty]""".stripMargin


    val footer = "\n}"

    val result = header+projectDefs.mkString("\n")+"\n"+projectDependencies.mkString("\n")+footer

    val out = new FileWriter(outFile)
    out.write(result.toString)
    out.close()
  }

  def makeProjectBox(projectName: String) = s"""$projectName[label = "{$projectName||}"]"""


  /**
   * Get files recursively in a directory
   * @param file
   * @return
   */
  def getFiles(file: File): Array[File] = {
    file.listFiles.map(file => {
      if (file.isDirectory)
        getFiles(file)
      else
        Array(file)
    }
    ).flatten
  }
}


object GraphGenerator {
  def main(args: Array[String]) {
    new GraphGenerator().generate(args(0), args(1))
  }
}