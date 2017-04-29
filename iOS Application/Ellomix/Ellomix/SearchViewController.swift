//
//  SearchViewController.swift
//  Ellomix
//
//  Created by Kevin Avila on 4/20/17.
//  Copyright © 2017 Akshay Vyas. All rights reserved.
//

import UIKit
import Alamofire

class SearchViewController: UITableViewController, UISearchControllerDelegate, UISearchResultsUpdating {
    
    let YouTubeAPIKey = "AIzaSyDl9doicP6uc4cEVlRDiM7Ttgy-o7Hal3I"
    var youtubeSearchURL = "https://www.googleapis.com/youtube/v3/search"
    typealias JSONStandard = [String : AnyObject]
    var searchController:UISearchController?
    
    override func viewDidLoad() {
        
        
        // Search bar initialization
        searchController = UISearchController(searchResultsController: nil)
        searchController?.dimsBackgroundDuringPresentation = false
        searchController?.searchResultsUpdater = self
        tableView.tableHeaderView = searchController?.searchBar
        searchController?.delegate = self
        self.tableView.backgroundView = UIView()
    }
    
    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return 0
    }
    
    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        return UITableViewCell()
    }
    
    override func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        
    }
    
    //MARK: Searchbar
    func updateSearchResults(for searchController: UISearchController) {
        // In the future, maybe display results as the user types via a background thread.
        if (searchController.searchBar.text != nil) {
            let searchString = searchController.searchBar.text!
        }
    }
    
    //MARK: YouTube
    func youtubeRequest(query: String) {
        Alamofire.request(youtubeSearchURL, parameters: ["part":"snippet", "q":query, "key":YouTubeAPIKey]).responseJSON(completionHandler: { response in
            
            print(response)
            if let JSON = response.result.value as? [String:AnyObject] {
                print("YouTube JSON data: \(JSON)")
                
                for video in JSON["items"] as! NSArray {
                    print("Video: \(video)")
                    
//                    let videoID = video.valueForKeyPath("snippet.id.videoId") as! String
//                    let videoTitle = video.valueForKeyPath("snippet.title") as! String
//                    let videoDescription = video.valueForKeyPath("snippet.description") as! String
//                    let videoThumbnailURL = video.valueForKeyPath("snippet.thumbnails.high.url") as! String
                }
            }
        })
    }
    
}
