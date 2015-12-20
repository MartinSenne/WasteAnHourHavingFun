package org.somuchfun.rockpaperscissors.ui

import org.somuchfun.rockpaperscissors.Report

/**
  * Created by martin on 20.12.15.
  */
trait ReportView {
  def show(report: Report, playerAName: String, playerBName: String, elements: Map[Int, String])
}
